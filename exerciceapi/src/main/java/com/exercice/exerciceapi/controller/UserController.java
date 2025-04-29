package com.exercice.exerciceapi.controller;

import com.exercice.exerciceapi.model.ChangePasswordRequest;
import com.exercice.exerciceapi.model.LoginRequest;
import com.exercice.exerciceapi.model.User;
import com.exercice.exerciceapi.model.UserResponseDto;
import com.exercice.exerciceapi.repository.UserRepository;
import com.exercice.exerciceapi.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Optional;


import java.util.List;
import java.util.stream.Collectors;



@RestController
public class UserController {

    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;

    @Autowired
    public UserController(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/user/{id}")
    public User getUserById(@PathVariable Long id){
        return userRepository.findById(id).orElse(null);
    }

    @PostMapping("/users")
    public ResponseEntity<String> createUser(@RequestBody User user){
        Optional<User> existingUser = Optional.ofNullable(userRepository.findDirectByEmail(user.getEmail()));
        if (existingUser.isPresent()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exist with this email");
        }

        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("User have been created!"); //Serveur renvoie une resp donc on modifier cette response pour quelle corresponde à ce qu'on vient de faire,
        // permet d'éviter les erreurs/ de les répertorier, renvoie aussi la data, etc
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long id){
        Optional<User> userToDelete = userRepository.findById(id);
        if ( userToDelete.isEmpty() ){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        userRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully");
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Optional<User> optionalUser = userRepository.findByEmail(loginRequest.getEmail());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getPassword().equals(loginRequest.getPassword())) {
                // Génération du token
                String token = jwtUtil.generateToken(user.getEmail());

                // Retourne le token dans un objet JSON
                return ResponseEntity.ok().body("{\"token\":\"" + token + "\"}");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong password");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }


    @GetMapping("/users")
    public List<UserResponseDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> new UserResponseDto(user.getId(), user.getEmail(), user.getName()))
                .collect(Collectors.toList());
    }

    @PutMapping("/users/{id}/password")
    public ResponseEntity<String> updatePassword(
            @PathVariable Long id,
            @RequestBody ChangePasswordRequest request) {

        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        User user = optionalUser.get();

        if (!user.getPassword().equals(request.getOldPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect old password.");
        }

        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The new passwords do not match.");
        }

        user.setPassword(request.getNewPassword());
        userRepository.save(user);

        return ResponseEntity.ok("Password successfully updated !");
    }

}



