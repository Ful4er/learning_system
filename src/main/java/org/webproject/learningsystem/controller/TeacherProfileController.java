package org.webproject.learningsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.webproject.learningsystem.model.User;
import org.webproject.learningsystem.service.ExamService;
import org.webproject.learningsystem.service.UserService;

@Controller
@RequestMapping("/teacher") // Базовый URL для профиля преподавателя.
public class TeacherProfileController {
    private final ExamService examService;

    public TeacherProfileController(UserService userService, ExamService examService) {
        this.examService = examService;
    }

    @GetMapping("/profile") // Отображает профиль преподавателя.
    public String showProfile(HttpSession session, Model model) {
        User teacher = (User) session.getAttribute("user");
        if (teacher == null || teacher.getRole() != User.Role.TEACHER) {
            return "redirect:/auth"; // Перенаправляем на авторизацию, если пользователь не преподаватель.
        }

        int examCount = examService.findByTeacher(teacher).size(); // Количество экзаменов преподавателя.
        int studentCount = examService.countUniqueStudentsByTeacher(teacher); // Количество уникальных студентов.

        model.addAttribute("user", teacher);
        model.addAttribute("examCount", examCount); // Добавляем статистику в модель.
        model.addAttribute("studentCount", studentCount);
        return "teacher/profile"; // Возвращает представление профиля преподавателя.
    }
}