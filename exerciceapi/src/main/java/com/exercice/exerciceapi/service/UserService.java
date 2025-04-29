package com.example.demo.service;


import com.exercice.exerciceapi.model.User;
import com.exercice.exerciceapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean changePassword(String email, String oldPassword, String newPassword, String confirmNewPassword) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        // Vérifie si l'utilisateur existe
        if (!optionalUser.isPresent()) {
            return false;
        }

        User user = optionalUser.get();

        // Vérifie si l'ancien mot de passe est correct
        if (!user.getPassword().equals(oldPassword)) {
            return false;
        }

        // Vérifie si les nouveaux mots de passe correspondent
        if (!newPassword.equals(confirmNewPassword)) {
            return false;
        }

        // Change le mot de passe
        user.setPassword(newPassword);
        userRepository.save(user);

        return true;
    }
}


