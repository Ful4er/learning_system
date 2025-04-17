package org.webproject.learningsystem.controller;

import org.webproject.learningsystem.model.Exam;
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
@RequestMapping("/teacher/students") // Базовый URL для управления студентами преподавателя.
public class TeacherStudentsController {
    private final ExamService examService;
    private final UserService userService;

    public TeacherStudentsController(ExamService examService, UserService userService) {
        this.examService = examService;
        this.userService = userService;
    }

    @GetMapping // Отображает список студентов преподавателя.
    public String listStudents(HttpSession session, Model model) {
        User teacher = (User) session.getAttribute("user");
        if (teacher == null || teacher.getRole() != User.Role.TEACHER) {
            return "redirect:/auth"; // Перенаправляем на авторизацию, если пользователь не преподаватель.
        }

        Set<User> students = examService.findByTeacher(teacher).stream()
                .flatMap(exam -> exam.getStudents().stream()) // Получаем всех уникальных студентов из экзаменов.
                .collect(Collectors.toSet());

        model.addAttribute("students", students); // Добавляем студентов в модель.
        model.addAttribute("user", teacher);
        return "teacher/students"; // Возвращает представление списка студентов.
    }

    @GetMapping("/{studentId}") // Отображает детали студента.
    public String getStudentDetails(@PathVariable Long studentId,
                                    HttpSession session,
                                    Model model) {
        User teacher = (User) session.getAttribute("user");
        if (teacher == null || teacher.getRole() != User.Role.TEACHER) {
            return "redirect:/auth"; // Проверка прав доступа.
        }

        User student = userService.findById(studentId); // Находим студента по ID.
        if (student == null || student.getRole() != User.Role.STUDENT) {
            return "redirect:/teacher/students"; // Перенаправляем, если студент не найден.
        }

        List<Exam> teacherExams = examService.findByTeacherWithStudents(teacher); // Экзамены преподавателя.
        List<ExamStudent> examResults = examService.getExamResultsForStudent(student); // Результаты студента.

        model.addAttribute("student", student); // Добавляем данные в модель.
        model.addAttribute("examResults", examResults);
        return "teacher/student-details-modal"; // Возвращает модальное окно с деталями студента.
    }
}