package org.webproject.learningsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.webproject.learningsystem.model.Exam;
import org.webproject.learningsystem.model.User;
import java.util.List;

public interface ExamRepository extends JpaRepository<Exam, Long> {
    // Метод для поиска всех экзаменов, созданных определенным преподавателем.
    List<Exam> findByTeacher(User teacher);

    // Метод для поиска всех экзаменов, на которые записан определенный студент.
    List<Exam> findByStudentsContaining(User student);
}