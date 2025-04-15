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
@RequestMapping("/teacher/exams") // Базовый URL для управления экзаменами преподавателя.
public class ExamController {
    private final ExamService examService;

    public ExamController(ExamService examService) {
        this.examService = examService;
    }

    @GetMapping // Отображает список экзаменов для преподавателя.
    public String listExams(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login"; // Перенаправляем на авторизацию, если пользователь не найден.
        }

        if (user.getRole() == User.Role.TEACHER) {
            model.addAttribute("exams", examService.findByTeacher(user)); // Добавляем экзамены в модель.
            model.addAttribute("isTeacher", true); // Флаг для шаблона.
            model.addAttribute("newExam", new Exam()); // Объект для формы создания нового экзамена.
        } else {
            return "redirect:/student/exams"; // Редирект для студентов.
        }

        model.addAttribute("user", user);
        return "teacher/exams"; // Возвращает представление для преподавателя.
    }

    @PostMapping // Создает новый экзамен.
    public String createExam(@ModelAttribute Exam newExam,
                             HttpSession session) {
        User teacher = (User) session.getAttribute("user");
        if (teacher == null || teacher.getRole() != User.Role.TEACHER) {
            return "redirect:/auth/login"; // Проверка прав доступа.
        }

        newExam.setTeacher(teacher); // Устанавливаем преподавателя для экзамена.
        examService.save(newExam); // Сохраняем экзамен через сервис.
        return "redirect:/teacher/exams"; // Перенаправляем на список экзаменов.
    }

    @PostMapping("/{examId}/enroll") // Записывает студента на экзамен.
    public String enrollStudent(@PathVariable Long examId,
                                HttpSession session) {
        User student = (User) session.getAttribute("user");
        if (student == null || student.getRole() != User.Role.STUDENT) {
            return "redirect:/auth/login"; // Проверка прав доступа.
        }

        examService.enrollStudent(examId, student); // Записываем студента через сервис.
        return "redirect:/student/exams"; // Перенаправляем на список экзаменов студента.
    }

    @PostMapping("/{examId}/complete") // Завершает экзамен и сохраняет результат.
    public String completeExam(@PathVariable Long examId,
                               @RequestParam Integer score,
                               HttpSession session) {
        User student = (User) session.getAttribute("user");
        if (student == null || student.getRole() != User.Role.STUDENT) {
            return "redirect:/auth/login"; // Проверка прав доступа.
        }

        examService.recordExamResult(examId, student, score); // Сохраняем результат через сервис.
        return "redirect:/student/exams"; // Перенаправляем на список экзаменов студента.
    }
}