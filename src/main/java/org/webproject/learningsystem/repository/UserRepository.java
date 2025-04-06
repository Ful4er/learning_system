package org.webproject.learningsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.webproject.learningsystem.model.User;
import org.webproject.learningsystem.model.User.Role;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    User findByRole(Role role);
}