package org.webproject.learningsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.webproject.learningsystem.model.User;

@Controller
@RequestMapping("/student") // Базовый URL для профиля студента.
public class StudentProfileController {

    @GetMapping("/profile") // Отображает профиль студента.
    public String showProfile(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != User.Role.STUDENT) {
            return "redirect:/auth"; // Перенаправляем на авторизацию, если пользователь не студент.
        }

        model.addAttribute("user", user); // Добавляем пользователя в модель.
        return "student/profile"; // Возвращает представление профиля студента.
    }
}