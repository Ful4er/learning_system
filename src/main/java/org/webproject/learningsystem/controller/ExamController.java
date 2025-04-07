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
@RequestMapping("/exams")
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
        } else {
            model.addAttribute("exams", examService.findByStudent(user));
            model.addAttribute("isTeacher", false);

            User teacher = userService.findTeacher();
            if (teacher != null) {
                List<Exam> allExams = examService.findByTeacher(teacher);
                allExams.removeAll(examService.findByStudent(user));
                model.addAttribute("availableExams", allExams);
            }
        }

        model.addAttribute("user", user);
        return "exams";
    }

    @PostMapping
    public String createExam(@RequestParam String title,
                             @RequestParam String description,
                             @RequestParam Integer duration,
                             @RequestParam Integer passingScore,
                             HttpSession session) {
        User teacher = (User) session.getAttribute("user");
        if (teacher == null || teacher.getRole() != User.Role.TEACHER) {
            return "redirect:/auth/login";
        }

        Exam exam = new Exam();
        exam.setTitle(title);
        exam.setDescription(description);
        exam.setTeacher(teacher);
        exam.setDurationMinutes(duration);
        exam.setPassingScore(passingScore);
        examService.save(exam);

        return "redirect:/exams";
    }

    @PostMapping("/{examId}/enroll")
    public String enrollStudent(@PathVariable Long examId,
                                HttpSession session) {
        User student = (User) session.getAttribute("user");
        if (student == null || student.getRole() != User.Role.STUDENT) {
            return "redirect:/auth/login";
        }

        examService.enrollStudent(examId, student);
        return "redirect:/exams";
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
        return "redirect:/exams";
    }
}