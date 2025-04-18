package org.webproject.learningsystem.controller;

import org.webproject.learningsystem.model.ExamStudent;
import org.webproject.learningsystem.model.User;
import org.webproject.learningsystem.service.ExamService;
import org.webproject.learningsystem.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/teacher/students")
public class TeacherStudentsController {
    private final ExamService examService;
    private final UserService userService;

    public TeacherStudentsController(ExamService examService, UserService userService) {
        this.examService = examService;
        this.userService = userService;
    }

    @GetMapping
    public String listStudents(HttpSession session, Model model) {
        User teacher = (User) session.getAttribute("user");
        if (teacher == null || teacher.getRole() != User.Role.TEACHER) {
            return "redirect:/auth";
        }

        Set<User> students = examService.findByTeacher(teacher).stream()
                .flatMap(exam -> exam.getStudents().stream())
                .collect(Collectors.toSet());

        model.addAttribute("students", students);
        model.addAttribute("user", teacher);
        return "teacher/students";
    }

    @GetMapping("/{studentId}")
    public String getStudentDetails(@PathVariable Long studentId,
                                    HttpSession session,
                                    Model model) {
        User teacher = (User) session.getAttribute("user");
        if (teacher == null || teacher.getRole() != User.Role.TEACHER) {
            return "redirect:/auth";
        }

        User student = userService.findById(studentId);
        if (student == null || student.getRole() != User.Role.STUDENT) {
            return "redirect:/teacher/students";
        }

        List<ExamStudent> examResults = examService.getExamResultsForStudent(student);

        model.addAttribute("student", student);
        model.addAttribute("examResults", examResults);
        return "teacher/student-details-modal";
    }
}