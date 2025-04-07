package org.webproject.learningsystem.service;

import org.webproject.learningsystem.model.Exam;
import org.webproject.learningsystem.model.User;
import org.webproject.learningsystem.repository.ExamRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public void recordExamResult(Long examId, User student, Integer score) {
        // Здесь будет логика записи результатов экзамена
    }
}