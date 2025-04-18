package org.webproject.learningsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.webproject.learningsystem.model.User;

@Controller
@RequestMapping("/student")
public class StudentProfileController {

    @GetMapping("/profile")
    public String showProfile(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != User.Role.STUDENT) {
            return "redirect:/auth";
        }

        model.addAttribute("user", user);
        return "student/profile";
    }
}