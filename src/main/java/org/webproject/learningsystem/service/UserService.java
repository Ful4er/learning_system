package org.webproject.learningsystem.service;

import org.springframework.stereotype.Service; // Аннотация указывает, что это сервисный компонент Spring [[1]].
import org.webproject.learningsystem.model.User;
import org.webproject.learningsystem.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository; // Репозиторий для работы с пользователями.

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Метод для поиска пользователя по email и паролю.
    public User findByEmailAndPassword(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) { // Проверка совпадения пароля [[2]].
            return user;
        }
        return null; // Если пользователь не найден или пароль неверный, возвращается null.
    }

    // Метод для поиска пользователя по email.
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Метод для поиска преподавателя по роли.
    public User findTeacher() {
        return userRepository.findByRole(User.Role.TEACHER);
    }

    // Метод для сохранения нового пользователя.
    public void save(User user) {
        userRepository.save(user);
    }

    // Метод для поиска пользователя по ID.
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null); // Возвращает пользователя или null, если ID не найден.
    }
}