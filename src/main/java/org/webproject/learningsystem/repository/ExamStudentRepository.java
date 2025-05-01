package org.webproject.learningsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.webproject.learningsystem.model.Exam;
import org.webproject.learningsystem.model.ExamStudent;
import org.webproject.learningsystem.model.ExamStudentId;
import org.webproject.learningsystem.model.User;

import java.util.List;

public interface ExamStudentRepository extends JpaRepository<ExamStudent, ExamStudentId> {
    List<ExamStudent> findByStudent(User student);
    List<ExamStudent> findByExam(Exam exam);
    boolean existsByExamAndStudent(Exam exam, User student);
    void deleteByExamAndStudent(Exam exam, User student);
}