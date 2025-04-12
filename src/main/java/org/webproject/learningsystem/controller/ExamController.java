package org.webproject.learningsystem.controller;

import org.webproject.learningsystem.model.Exam;
import org.webproject.learningsystem.model.User;
import org.webproject.learningsystem.service.ExamService;
import org.webproject.learningsystem.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/teacher/exams")
public class ExamController {
    private final ExamService examService;
    private final UserService userService;

    public ExamController(ExamService examService, UserService userService) {
        this.examService = examService;
        this.userService = userService;
    }

    @GetMapping
    public String listExams(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }

        if (user.getRole() == User.Role.TEACHER) {
            model.addAttribute("exams", examService.findByTeacher(user));
            model.addAttribute("isTeacher", true);
            model.addAttribute("newExam", new Exam()); // Добавляем для формы создания
        } else {
            // Редирект для студентов, если они попали сюда
            return "redirect:/student/exams";
        }

        model.addAttribute("user", user);
        return "teacher/exams";
    }

    @PostMapping
    public String createExam(@ModelAttribute Exam newExam,
                             HttpSession session) {
        User teacher = (User) session.getAttribute("user");
        if (teacher == null || teacher.getRole() != User.Role.TEACHER) {
            return "redirect:/auth/login";
        }

        newExam.setTeacher(teacher);
        examService.save(newExam);
        return "redirect:/teacher/exams";
    }

    @PostMapping("/{examId}/enroll")
    public String enrollStudent(@PathVariable Long examId,
                                HttpSession session) {
        User student = (User) session.getAttribute("user");
        if (student == null || student.getRole() != User.Role.STUDENT) {
            return "redirect:/auth/login";
        }

        examService.enrollStudent(examId, student);
        return "redirect:/student/exams";
    }

    @PostMapping("/{examId}/complete")
    public String completeExam(@PathVariable Long examId,
                               @RequestParam Integer score,
                               HttpSession session) {
        User student = (User) session.getAttribute("user");
        if (student == null || student.getRole() != User.Role.STUDENT) {
            return "redirect:/auth/login";
        }

        examService.recordExamResult(examId, student, score);
        return "redirect:/student/exams";
    }
}