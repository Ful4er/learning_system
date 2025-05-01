package org.webproject.learningsystem.service;

import jakarta.transaction.Transactional;
import org.webproject.learningsystem.model.Exam;
import org.webproject.learningsystem.model.ExamStudent;
import org.webproject.learningsystem.model.User;
import org.webproject.learningsystem.repository.ExamRepository;
import org.springframework.stereotype.Service;
import org.webproject.learningsystem.repository.ExamStudentRepository;
import org.webproject.learningsystem.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ExamService {
    private final ExamRepository examRepository;
    private final ExamStudentRepository examStudentRepository;
    private final UserRepository userRepository;

    public ExamService(ExamRepository examRepository, ExamStudentRepository examStudentRepository, UserRepository userRepository) {
        this.examRepository = examRepository;
        this.examStudentRepository = examStudentRepository;
        this.userRepository = userRepository;
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
    @Transactional
    public void addStudentToExam(Long examId, String studentEmail, User teacher) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new IllegalArgumentException("Exam not found"));

        User student = userRepository.findByEmail(studentEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!exam.getTeacher().getId().equals(teacher.getId())) {
            throw new IllegalArgumentException("No permission");
        }

        if (student.getRole() != User.Role.STUDENT) {
            throw new IllegalArgumentException("Not a student");
        }

        boolean exists = exam.getStudents().contains(student) ||
                examStudentRepository.existsByExamAndStudent(exam, student);

        if (exists) {
            throw new IllegalArgumentException("Student already enrolled");
        }

        exam.getStudents().add(student);
        examRepository.save(exam);
    }
    @Transactional
    public void removeStudentFromExam(Long examId, Long studentId, User teacher) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new IllegalArgumentException("Exam not found"));

        if (!exam.getTeacher().getId().equals(teacher.getId())) {
            throw new IllegalArgumentException("No permission");
        }

        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        if (!exam.getStudents().remove(student)) {
            throw new IllegalArgumentException("Student not enrolled");
        }

        examStudentRepository.deleteByExamAndStudent(exam, student);

        examRepository.save(exam);
    }
}