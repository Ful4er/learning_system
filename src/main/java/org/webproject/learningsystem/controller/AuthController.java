package org.webproject.learningsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import org.webproject.learningsystem.model.User;
import org.webproject.learningsystem.service.UserService;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showAuthForm(@RequestParam(required = false) String error,
                               Model model) {
        if (error != null) {
            model.addAttribute("error", "Invalid email or password");
        }
        return "auth/home";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session) {
        User user = userService.findByEmailAndPassword(username, password);
        if (user == null) {
            return "redirect:/auth?error";
        }

        session.setAttribute("user", user);
        return user.getRole() == User.Role.TEACHER ? "redirect:/teacher/profile" : "redirect:/student/profile";
    }

    @PostMapping("/register")
    public String register(@RequestParam String firstName,
                           @RequestParam String lastName,
                           @RequestParam String email,
                           @RequestParam String password,
                           @RequestParam User.Role role,
                           HttpSession session) {

        if (userService.findByEmail(email) != null) {
            return "redirect:/auth?registerError";
        }

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role);

        userService.save(user);
        session.setAttribute("user", user);
        return role == User.Role.TEACHER ? "redirect:/teacher/profile" : "redirect:/student/profile";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/auth";
    }
}