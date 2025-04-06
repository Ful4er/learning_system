package org.webproject.learningsystem.repository;

import org.webproject.learningsystem.model.Course;
import org.webproject.learningsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByTeacher(User teacher);
    List<Course> findByStudentsContaining(User student);
}