package org.webproject.learningsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Entity
@Table(name = "users") // Таблица для пользователей системы.
@Data // Генерирует геттеры, сеттеры, equals, hashCode и toString методы [[1]].
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Автоматическая генерация ID.
    private Long id;

    @Column(nullable = false, unique = true) // Email должен быть уникальным.
    private String email;

    @Column(nullable = false) // Пароль пользователя.
    private String password;

    @Column(name = "first_name", nullable = false) // Имя пользователя.
    private String firstName;

    @Column(name = "last_name", nullable = false) // Фамилия пользователя.
    private String lastName;

    @Enumerated(EnumType.STRING) // Перечисление для роли пользователя (TEACHER или STUDENT).
    @Column(nullable = false)
    private Role role;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP) // Хранение даты и времени создания пользователя.
    private Date createdAt = new Date(); // Устанавливается автоматически при создании объекта.

    public enum Role {
        TEACHER, STUDENT // Возможные роли пользователя.
    }
}