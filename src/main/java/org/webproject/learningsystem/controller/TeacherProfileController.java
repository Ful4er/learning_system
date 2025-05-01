package org.webproject.learningsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.webproject.learningsystem.model.User;
import org.webproject.learningsystem.service.ExamService;

@Controller
@RequestMapping("/teacher")
public class TeacherProfileController {
    private final ExamService examService;

    public TeacherProfileController(ExamService examService) {
        this.examService = examService;
    }

    @GetMapping("/profile")
    public String showProfile(HttpSession session, Model model) {
        User teacher = (User) session.getAttribute("user");
        if (teacher == null || teacher.getRole() != User.Role.TEACHER) {
            return "redirect:/auth";
        }

        int examCount = examService.findByTeacher(teacher).size();
        int studentCount = examService.countUniqueStudentsByTeacher(teacher);

        model.addAttribute("user", teacher);
        model.addAttribute("examCount", examCount);
        model.addAttribute("studentCount", studentCount);
        return "teacher/profile";
    }
}