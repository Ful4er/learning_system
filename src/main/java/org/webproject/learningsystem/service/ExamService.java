package org.webproject.learningsystem.service;

import org.webproject.learningsystem.model.Exam;
import org.webproject.learningsystem.model.ExamStudent;
import org.webproject.learningsystem.model.User;
import org.webproject.learningsystem.repository.ExamRepository;
import org.springframework.stereotype.Service;
import org.webproject.learningsystem.repository.ExamStudentRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service // Аннотация указывает, что это сервисный компонент Spring [[1]].
public class ExamService {
    private final ExamRepository examRepository; // Репозиторий для работы с экзаменами.
    private final ExamStudentRepository examStudentRepository; // Репозиторий для работы с записями студентов на экзамены.

    public ExamService(ExamRepository examRepository, ExamStudentRepository examStudentRepository) {
        this.examRepository = examRepository;
        this.examStudentRepository = examStudentRepository;
    }

    // Метод для поиска всех экзаменов, созданных определенным преподавателем.
    public List<Exam> findByTeacher(User teacher) {
        return examRepository.findByTeacher(teacher);
    }

    // Метод для поиска всех экзаменов, на которые записан определенный студент.
    public List<Exam> findByStudent(User student) {
        return examRepository.findByStudentsContaining(student);
    }

    // Метод для сохранения нового экзамена.
    public void save(Exam exam) {
        examRepository.save(exam);
    }

    // Метод для записи студента на экзамен.
    public void enrollStudent(Long examId, User student) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new IllegalArgumentException("Exam not found")); // Бросает исключение, если экзамен не найден.
        exam.getStudents().add(student); // Добавляет студента в список участников экзамена.
        examRepository.save(exam); // Сохраняет обновленный экзамен.
    }

    // Метод для подсчета уникальных студентов у преподавателя.
    public int countUniqueStudentsByTeacher(User teacher) {
        Set<User> uniqueStudents = new HashSet<>(); // Используется для хранения уникальных студентов.
        for (Exam exam : findByTeacher(teacher)) {
            uniqueStudents.addAll(exam.getStudents()); // Добавляем всех студентов из экзаменов преподавателя.
        }
        return uniqueStudents.size(); // Возвращает количество уникальных студентов.
    }

    // Метод для записи результатов экзамена (логика пока не реализована).
    public void recordExamResult(Long examId, User student, Integer score) {
        // Здесь будет логика записи результатов экзамена.
    }

    // Метод для подсчета количества студентов на конкретном экзамене.
    public int countStudentsForExam(Long examId) {
        return examRepository.findById(examId)
                .map(exam -> exam.getStudents().size()) // Если экзамен найден, возвращает количество студентов.
                .orElse(0); // Если экзамен не найден, возвращает 0.
    }

    // Метод для подсчета количества экзаменов, на которые записан студент.
    public int countExamsForStudent(User student) {
        return examRepository.findByStudentsContaining(student).size();
    }

    // Метод для поиска всех экзаменов преподавателя вместе со списком студентов.
    public List<Exam> findByTeacherWithStudents(User teacher) {
        return examRepository.findByTeacher(teacher);
    }

    // Метод для получения результатов экзаменов конкретного студента.
    public List<ExamStudent> getExamResultsForStudent(User student) {
        return examStudentRepository.findByStudent(student);
    }

    public Optional<Exam> findById(Long examId) {
        return examRepository.findById(examId);
    }
}