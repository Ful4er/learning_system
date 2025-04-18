package org.webproject.learningsystem.controller;

import org.webproject.learningsystem.dto.ExamWithResults;
import org.webproject.learningsystem.model.Exam;
import org.webproject.learningsystem.model.ExamStudent;
import org.webproject.learningsystem.model.User;
import org.webproject.learningsystem.repository.ExamStudentRepository;
import org.webproject.learningsystem.service.ExamService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/teacher/exams")
public class TeacherExamController {
    private final ExamService examService;
    private ExamStudentRepository examStudentRepository;

    public TeacherExamController(ExamService examService, ExamStudentRepository examStudentRepository) {
        this.examService = examService;
        this.examStudentRepository = examStudentRepository;
    }

    @GetMapping
    public String listExams(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth";
        }

        if (user.getRole() == User.Role.TEACHER) {
            model.addAttribute("exams", examService.findByTeacher(user));
            model.addAttribute("isTeacher", true);
            model.addAttribute("newExam", new Exam());
        } else {
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
            return "redirect:/auth";
        }

        newExam.setTeacher(teacher);
        examService.save(newExam);
        return "redirect:/teacher/exams";
    }

    @GetMapping("/{examId}/details")
    @ResponseBody
    public ExamWithResults getExamDetails(@PathVariable Long examId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != User.Role.TEACHER) {
            throw new IllegalArgumentException("Unauthorized");
        }

        Exam exam = examService.findById(examId)
                .orElseThrow(() -> new IllegalArgumentException("Exam not found"));

        if (!exam.getTeacher().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Unauthorized");
        }

        List<ExamStudent> examStudents = examStudentRepository.findByExam(exam);

        ExamWithResults result = new ExamWithResults();
        result.setExam(exam);

        result.setStudentResults(examStudents.stream().map(es -> {
            ExamWithResults.StudentResult sr = new ExamWithResults.StudentResult();
            sr.setStudentId(es.getStudent().getId());
            sr.setStudentName(es.getStudent().getFirstName() + " " + es.getStudent().getLastName());
            sr.setStudentEmail(es.getStudent().getEmail());
            sr.setScore(es.getScore());
            sr.setCompletedAt(es.getCompletedAt() != null ? es.getCompletedAt().toString() : null);
            sr.setEnrolledAt(es.getEnrolledAt().toString());
            return sr;
        }).collect(Collectors.toList()));

        return result;
    }
}