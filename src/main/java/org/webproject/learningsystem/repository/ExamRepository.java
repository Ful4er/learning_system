package org.webproject.learningsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.webproject.learningsystem.model.Exam;
import org.webproject.learningsystem.model.User;

import java.util.List;

public interface ExamRepository extends JpaRepository<Exam, Long> {
    List<Exam> findByTeacher(User teacher);
    List<Exam> findByStudentsContaining(User student);
}