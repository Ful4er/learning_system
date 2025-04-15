package org.webproject.learningsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.webproject.learningsystem.model.User;
import org.webproject.learningsystem.model.User.Role;

public interface UserRepository extends JpaRepository<User, Long> {
    // Метод для поиска пользователя по email.
    User findByEmail(String email);

    // Метод для поиска пользователей по роли (например, TEACHER или STUDENT).
    User findByRole(Role role);
}