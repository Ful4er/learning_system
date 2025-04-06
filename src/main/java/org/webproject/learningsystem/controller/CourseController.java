package org.webproject.learningsystem.controller;

import org.webproject.learningsystem.model.Course;
import org.webproject.learningsystem.model.User;
import org.webproject.learningsystem.service.CourseService;
import org.webproject.learningsystem.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/courses")
public class CourseController {
    private final CourseService courseService;
    private final UserService userService;

    public CourseController(CourseService courseService, UserService userService) {
        this.courseService = courseService;
        this.userService = userService;
    }

    @GetMapping
    public String listCourses(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }

        if (user.getRole() == User.Role.TEACHER) {
            model.addAttribute("courses", courseService.findByTeacher(user));
            model.addAttribute("isTeacher", true);
        } else {
            model.addAttribute("courses", courseService.findByStudent(user));
            model.addAttribute("isTeacher", false);

            // Для студентов - список всех курсов для записи
            User teacher = userService.findTeacher();
            if (teacher != null) {
                List<Course> allCourses = courseService.findByTeacher(teacher);
                allCourses.removeAll(courseService.findByStudent(user));
                model.addAttribute("availableCourses", allCourses);
            }
        }

        model.addAttribute("user", user);
        return "courses";
    }

    @PostMapping
    public String createCourse(@RequestParam String title,
                               @RequestParam String description,
                               HttpSession session) {
        User teacher = (User) session.getAttribute("user");
        if (teacher == null || teacher.getRole() != User.Role.TEACHER) {
            return "redirect:/auth/login";
        }

        Course course = new Course();
        course.setTitle(title);
        course.setDescription(description);
        course.setTeacher(teacher);
        courseService.save(course);

        return "redirect:/courses";
    }

    @PostMapping("/{courseId}/enroll")
    public String enrollStudent(@PathVariable Long courseId,
                                HttpSession session) {
        User student = (User) session.getAttribute("user");
        if (student == null || student.getRole() != User.Role.STUDENT) {
            return "redirect:/auth/login";
        }

        courseService.enrollStudent(courseId, student);
        return "redirect:/courses";
    }
}