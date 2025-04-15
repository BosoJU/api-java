package com.exercice.exerciceapi.model;

import jakarta.persistence.*;
import java.util.Optional;
import java.util.Optional;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "role", discriminatorType = DiscriminatorType.STRING)

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    private String name;

    private String email;

    private String password;

    public Long getId(){
        return id;
    }

    public String getEmail(){
        return email;
    }

    public String getPassword(){
        return password;
    }


    public String getName() {
        return name;
    }

    public void setPassword(String newPassword) {
    }
}
