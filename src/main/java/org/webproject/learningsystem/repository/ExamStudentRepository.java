package org.webproject.learningsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.webproject.learningsystem.model.Exam;
import org.webproject.learningsystem.model.ExamStudent;
import org.webproject.learningsystem.model.ExamStudentId;
import org.webproject.learningsystem.model.User;

import java.util.List;

public interface ExamStudentRepository extends JpaRepository<ExamStudent, ExamStudentId> {
    // Метод для поиска всех записей о студентах, связанных с конкретным студентом.
    List<ExamStudent> findByStudent(User student);

    // Метод для поиска всех записей о студентах, связанных с конкретным экзаменом.
    List<ExamStudent> findByExam(Exam exam);
}