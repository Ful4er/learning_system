document.getElementById('addExamBtn').onclick = function() {
    document.getElementById('examModal').style.display = 'block';
}

document.querySelector('#examModal .close').onclick = function() {
    document.getElementById('examModal').style.display = 'none';
}

function closeExamDetailsModal() {
    document.getElementById('examDetailsModal').style.display = 'none';
}

window.onclick = function(event) {
    if (event.target === document.getElementById('examModal')) {
        document.getElementById('examModal').style.display = 'none';
    }
    if (event.target === document.getElementById('examDetailsModal')) {
        closeExamDetailsModal();
    }
}

document.querySelectorAll('.exam-link').forEach(link => {
    link.addEventListener('click', function(e) {
        e.preventDefault();
        const examId = this.getAttribute('data-exam-id');
        fetchExamDetails(examId);
    });
});

function fetchExamDetails(examId) {
    document.getElementById('examDetailsModal').dataset.examId = examId;
    fetch(`/teacher/exams/${examId}/details`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(exam => {
            displayExamDetails(exam);
            document.getElementById('examDetailsModal').style.display = 'block';
        })
        .catch(error => {
            console.error('Error fetching exam details:', error);
            alert('Failed to load exam details');
        });
}
function addStudentToExam() {
    const emailInput = document.getElementById('studentEmail');
    const email = emailInput.value.trim();
    const examId = document.querySelector('#examDetailsModal').dataset.examId;
    const messageDiv = document.getElementById('addStudentMessage');

    if (!email) {
        showMessage('Please enter student email', 'error');
        return;
    }

    fetch(`/teacher/exams/${examId}/add-student`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ email: email })
    })
        .then(response => {
            if (response.ok) return;
            return response.text().then(text => {
                throw new Error(text || 'Failed to add student')
            });
        })
        .then(() => {
            showMessage('Student added successfully', 'success');
            emailInput.value = '';
            fetchExamDetails(examId);
        })
        .catch(error => {
            showMessage(error.message, 'error');
        });
}

function showMessage(text, type) {
    const messageDiv = document.getElementById('addStudentMessage');
    messageDiv.textContent = text;
    messageDiv.className = `message ${type}`;
    setTimeout(() => {
        messageDiv.textContent = '';
        messageDiv.className = 'message';
    }, 5000);
}
function displayExamDetails(data) {
    const exam = data.exam;
    const studentResults = data.studentResults;

    document.getElementById('examTitle').textContent = exam.title;
    document.getElementById('examDescription').textContent = exam.description || 'No description provided';
    document.getElementById('examTeacher').textContent = exam.teacher.firstName + ' ' + exam.teacher.lastName;
    document.getElementById('examCreatedAt').textContent = new Date(exam.createdAt).toLocaleString();
    document.getElementById('examDuration').textContent = `${exam.durationMinutes} minutes`;
    document.getElementById('examPassingScore').textContent = exam.passingScore;
    document.getElementById('examStudentsCount').textContent = studentResults.length;

    const studentsList = document.getElementById('studentsList');
    studentsList.innerHTML = '';

    if (studentResults.length === 0) {
        studentsList.innerHTML = '<tr><td colspan="5">No students enrolled yet</td></tr>';
    } else {
        studentResults.forEach(student => {
            const initials = student.studentName.split(' ')
                .map(n => n.charAt(0))
                .join('');

            const status = student.completedAt ?
                '<span class="status completed">Completed</span>' :
                '<span class="status pending">Pending</span>';

            const score = student.score || '-';
            const completedDate = student.completedAt ?
                new Date(student.completedAt).toLocaleString() : '-';

            const row = document.createElement('tr');
            row.innerHTML = `
                <td>
                    <div class="student-info">
                        <div class="student-avatar small">${initials}</div>
                        <div>
                            <div class="student-name">${student.studentName}</div>
                            <div class="student-email">${student.studentEmail}</div>
                        </div>
                    </div>
                </td>
                <td>${status}</td>
                <td>${score}</td>
                <td>${completedDate}</td>
                <td>
                    <button class="delete-btn" data-student-id="${student.studentId}">
                        <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                            <path d="M3 6h18M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"></path>
                        </svg>
                    </button>
                </td>
            `;

            row.querySelector('.delete-btn').addEventListener('click', () => {
                deleteStudent(exam.id, student.studentId);
            });

            studentsList.appendChild(row);
        });
    }
}

function deleteStudent(examId, studentId) {
    if (!confirm('Are you sure you want to remove this student?')) return;

    fetch(`/teacher/exams/${examId}/students/${studentId}`, {
        method: 'DELETE'
    })
        .then(response => {
            if (!response.ok) {
                return response.text().then(text => { throw new Error(text) });
            }
            fetchExamDetails(examId);
        })
        .catch(error => {
            showMessage(error.message || 'Failed to delete student', 'error');
        });
}