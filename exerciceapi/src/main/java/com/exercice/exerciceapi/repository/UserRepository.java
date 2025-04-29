package com.exercice.exerciceapi.repository;

import com.exercice.exerciceapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository <User, Long> {
    Optional<User> findByEmail(String email);
    User findDirectByEmail(String email);
}
