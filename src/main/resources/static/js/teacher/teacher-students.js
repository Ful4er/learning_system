function showStudentDetails(studentId) {
    fetch('/teacher/students/' + studentId)
        .then(response => response.text())
        .then(html => {
            document.getElementById('studentDetailsContent').innerHTML = html;
            document.getElementById('studentModal').style.display = 'block';
        });
}

function closeStudentModal() {
    document.getElementById('studentModal').style.display = 'none';
}

window.onclick = function(event) {
    if (event.target === document.getElementById('studentModal')) {
        closeStudentModal();
    }
}