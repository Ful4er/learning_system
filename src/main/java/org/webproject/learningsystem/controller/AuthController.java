package org.webproject.learningsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import org.webproject.learningsystem.model.User;
import org.webproject.learningsystem.service.UserService;

@Controller // Аннотация указывает, что это контроллер Spring MVC.
@RequestMapping("/auth") // Базовый URL для всех методов в этом контроллере.
public class AuthController {
    private final UserService userService; // Внедрение сервиса для работы с пользователями.

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping // Обрабатывает GET-запросы на "/auth".
    public String showAuthForm(@RequestParam(required = false) String error,
                               Model model) {
        // Если параметр "error" присутствует, добавляем сообщение об ошибке в модель.
        if (error != null) {
            model.addAttribute("error", "Invalid email or password");
        }
        return "auth/home"; // Возвращает имя представления Thymeleaf.
    }

    @PostMapping("/login") // Обрабатывает POST-запросы на "/auth/login".
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session) {
        // Проверяем учетные данные пользователя через сервис.
        User user = userService.findByEmailAndPassword(username, password);
        if (user == null) {
            return "redirect:/auth?error"; // Перенаправляем на форму авторизации с ошибкой.
        }

        session.setAttribute("user", user); // Сохраняем пользователя в сессии.
        // Перенаправляем в зависимости от роли пользователя.
        return user.getRole() == User.Role.TEACHER ? "redirect:/teacher/profile" : "redirect:/student/profile";
    }

    @PostMapping("/register") // Обрабатывает регистрацию нового пользователя.
    public String register(@RequestParam String firstName,
                           @RequestParam String lastName,
                           @RequestParam String email,
                           @RequestParam String password,
                           @RequestParam User.Role role,
                           HttpSession session) {

        // Проверяем, существует ли пользователь с таким email.
        if (userService.findByEmail(email) != null) {
            return "redirect:/auth?registerError"; // Перенаправляем с ошибкой.
        }

        User user = new User(); // Создаем нового пользователя.
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role);

        userService.save(user); // Сохраняем пользователя через сервис.
        session.setAttribute("user", user); // Добавляем пользователя в сессию.
        // Перенаправляем в зависимости от роли.
        return role == User.Role.TEACHER ? "redirect:/teacher/profile" : "redirect:/student/profile";
    }

    @GetMapping("/logout") // Обрабатывает выход из системы.
    public String logout(HttpSession session) {
        session.invalidate(); // Удаляем все атрибуты сессии.
        return "redirect:/auth"; // Перенаправляем на страницу авторизации.
    }
}