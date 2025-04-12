package org.webproject.learningsystem.service;
import org.springframework.stereotype.Service;
import org.webproject.learningsystem.model.User;
import org.webproject.learningsystem.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public User findByEmailAndPassword(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public User findTeacher() {
        return userRepository.findByRole(User.Role.TEACHER);
    }
    public void save(User user) {
        userRepository.save(user);
    }
}