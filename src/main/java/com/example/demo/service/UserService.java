package com.example.demo.service;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.exception.InvalidCredentialsException;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(RegisterRequest request) {
        User user=new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);

        return userRepository.save(user);
    }
    public User authenticate(LoginRequest request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() ->
                        new InvalidCredentialsException("Invalid username or password"));

        boolean passwordMatches = passwordEncoder.matches(
                request.getPassword(),// raw password from user
                user.getPassword() //BCrypt hash from database
        );

        if (!passwordMatches) {
            throw new InvalidCredentialsException("Invalid username or password");
        }

        return user;
    }
    //for refresh token cycle
    public User getUserByUsername(String username) {

        return userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));
    }
}


//never do this:
//        if (user.getPassword().equals(request.getPassword()));
//Instead, login will use:
//        passwordEncoder.matches(
//        rawPassword,
//        storedHashedPassword
//        );
//BCrypt handles the salt and hash comparison for us.


//BCrypt checks whether Password123 corresponds to that stored hash.
//We do not decode the BCrypt hash. BCrypt is designed to be one-way.
//Request:Password123
//Database:$2a$10$xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx