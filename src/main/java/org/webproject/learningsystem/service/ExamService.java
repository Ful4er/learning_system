package org.webproject.learningsystem.service;

import org.webproject.learningsystem.model.Exam;
import org.webproject.learningsystem.model.ExamStudent;
import org.webproject.learningsystem.model.User;
import org.webproject.learningsystem.repository.ExamRepository;
import org.springframework.stereotype.Service;
import org.webproject.learningsystem.repository.ExamStudentRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ExamService {
    private final ExamRepository examRepository;
    private final ExamStudentRepository examStudentRepository;

    public ExamService(ExamRepository examRepository, ExamStudentRepository examStudentRepository) {
        this.examRepository = examRepository;
        this.examStudentRepository = examStudentRepository;
    }
    public List<Exam> findByTeacher(User teacher) {
        return examRepository.findByTeacher(teacher);
    }
    public void save(Exam exam) {
        examRepository.save(exam);
    }
    public int countUniqueStudentsByTeacher(User teacher) {
        Set<User> uniqueStudents = new HashSet<>();
        for (Exam exam : findByTeacher(teacher)) {
            uniqueStudents.addAll(exam.getStudents());
        }
        return uniqueStudents.size();
    }
    public List<ExamStudent> getExamResultsForStudent(User student) {
        return examStudentRepository.findByStudent(student);
    }

    public Optional<Exam> findById(Long examId) {
        return examRepository.findById(examId);
    }
}