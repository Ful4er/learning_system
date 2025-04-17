// Обработчик для модального окна добавления экзамена
document.getElementById('addExamBtn').onclick = function() {
    document.getElementById('examModal').style.display = 'block';
}

document.querySelector('#examModal .close').onclick = function() {
    document.getElementById('examModal').style.display = 'none';
}

// Обработчики для модального окна деталей экзамена
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

// Обработчик кликов по карточкам экзаменов
document.querySelectorAll('.exam-link').forEach(link => {
    link.addEventListener('click', function(e) {
        e.preventDefault();
        const examId = this.getAttribute('data-exam-id');
        fetchExamDetails(examId);
    });
});

// Функция для загрузки деталей экзамена
function fetchExamDetails(examId) {
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

// Функция для отображения деталей экзамена
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
        studentsList.innerHTML = '<tr><td colspan="4">No students enrolled yet</td></tr>';
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
            `;
            studentsList.appendChild(row);
        });
    }
}