package org.webproject.learningsystem.service;
import org.webproject.learningsystem.model.Exam;
import org.webproject.learningsystem.model.User;
import org.webproject.learningsystem.repository.ExamRepository;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ExamService {
    private final ExamRepository examRepository;
    public ExamService(ExamRepository examRepository) {
        this.examRepository = examRepository;
    }
    public List<Exam> findByTeacher(User teacher) {
        return examRepository.findByTeacher(teacher);
    }
    public List<Exam> findByStudent(User student) {
        return examRepository.findByStudentsContaining(student);
    }
    public Exam save(Exam exam) {
        return examRepository.save(exam);
    }
    public void enrollStudent(Long examId, User student) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new IllegalArgumentException("Exam not found"));
        exam.getStudents().add(student);
        examRepository.save(exam);
    }
    public int countUniqueStudentsByTeacher(User teacher) {
        Set<User> uniqueStudents = new HashSet<>();
        for (Exam exam : findByTeacher(teacher)) {
            uniqueStudents.addAll(exam.getStudents());
        }
        return uniqueStudents.size();
    }
    public void recordExamResult(Long examId, User student, Integer score) {
        // Здесь будет логика записи результатов экзамена
    }
    public int countStudentsForExam(Long examId) {
        return examRepository.findById(examId)
                .map(exam -> exam.getStudents().size())
                .orElse(0);
    }
    public int countExamsForStudent(User student) {
        return examRepository.findByStudentsContaining(student).size();
    }
    public List<Exam> findByTeacherWithStudents(User teacher) {
        return examRepository.findByTeacher(teacher);
    }
}