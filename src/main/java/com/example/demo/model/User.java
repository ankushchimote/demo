package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;//this field will eventually contain the BCrypt hashed password, not the user's actual password.

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
}

//@Column(nullable = false)
//private String password;
//User enters:
//MyPassword123
//
//Database stores something like:
//$2a$10$...



//@Enumerated(EnumType.STRING)
//private Role role;
//Without EnumType.STRING, JPA can store the enum as an integer: 1,2,4,56,
//
//with @Enumerated(EnumType.STRING), db will store ike: USER,ADMIN