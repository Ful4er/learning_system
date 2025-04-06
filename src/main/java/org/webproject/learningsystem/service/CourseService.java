package org.webproject.learningsystem.service;

import org.webproject.learningsystem.model.Course;
import org.webproject.learningsystem.model.User;
import org.webproject.learningsystem.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {
    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> findByTeacher(User teacher) {
        return courseRepository.findByTeacher(teacher);
    }

    public List<Course> findByStudent(User student) {
        return courseRepository.findByStudentsContaining(student);
    }

    public Course save(Course course) {
        return courseRepository.save(course);
    }

    public void enrollStudent(Long courseId, User student) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));
        course.getStudents().add(student);
        courseRepository.save(course);
    }
}