package com.exercice.exerciceapi.model;

public class UserResponseDto {
    private Long id;
    private String email;
    private String name;

    // Constructeur
    public UserResponseDto(Long id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}
