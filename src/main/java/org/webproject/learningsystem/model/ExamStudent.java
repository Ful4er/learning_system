package org.webproject.learningsystem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "exam_students") // Таблица для связи экзаменов и студентов.
public class ExamStudent {
    @EmbeddedId // Первичный ключ, состоящий из двух полей (examId и studentId).
    private ExamStudentId id;

    @MapsId("examId") // Связь с полем examId в составном ключе.
    @ManyToOne(fetch = FetchType.LAZY, optional = false) // Ленивая загрузка данных.
    @OnDelete(action = OnDeleteAction.CASCADE) // Каскадное удаление при удалении экзамена.
    @JoinColumn(name = "exam_id", nullable = false) // Внешний ключ на таблицу экзаменов.
    private Exam exam;

    @MapsId("studentId") // Связь с полем studentId в составном ключе.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE) // Каскадное удаление при удалении студента.
    @JoinColumn(name = "student_id", nullable = false) // Внешний ключ на таблицу пользователей.
    private User student;

    @ColumnDefault("CURRENT_TIMESTAMP") // Значение по умолчанию — текущая временная метка.
    @Column(name = "enrolled_at")
    private Instant enrolledAt; // Время записи студента на экзамен.

    @Column(name = "completed_at")
    private Instant completedAt; // Время завершения экзамена.

    @Column(name = "score")
    private Integer score; // Результат экзамена (баллы).
}