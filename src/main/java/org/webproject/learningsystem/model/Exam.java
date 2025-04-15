package org.webproject.learningsystem.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "exams") // Таблица в базе данных для экзаменов.
@Data // Генерирует геттеры, сеттеры, equals, hashCode и toString методы [[1]].
@NoArgsConstructor // Генерирует конструктор без параметров [[2]].
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Автоматическая генерация ID.
    private Long id;

    @Column(nullable = false) // Поле не может быть null.
    private String title;

    private String description; // Описание экзамена.

    @ManyToOne // Связь многие-к-одному: один учитель может создать много экзаменов.
    @JoinColumn(name = "teacher_id", nullable = false) // Внешний ключ на таблицу пользователей.
    private User teacher;

    @ManyToMany // Связь многие-ко-многим: один экзамен может быть связан с множеством студентов.
    @JoinTable(
            name = "exam_students", // Промежуточная таблица для связи экзаменов и студентов.
            joinColumns = @JoinColumn(name = "exam_id"), // Столбец для ID экзамена.
            inverseJoinColumns = @JoinColumn(name = "student_id") // Столбец для ID студента.
    )
    private Set<User> students = new HashSet<>(); // Множество студентов, записанных на экзамен.

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP) // Хранение даты и времени создания экзамена.
    private Date createdAt = new Date(); // Устанавливается автоматически при создании объекта.

    @Column(name = "duration_minutes")
    private Integer durationMinutes; // Длительность экзамена в минутах.

    @Column(name = "passing_score")
    private Integer passingScore; // Минимальный проходной балл.
}