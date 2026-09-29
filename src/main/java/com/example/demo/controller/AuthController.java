package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.model.RefreshToken;
import com.example.demo.model.User;
import com.example.demo.service.JwtService;
import com.example.demo.service.RefreshTokenService;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final UserService userService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    public AuthController(UserService userService, JwtService jwtService, RefreshTokenService refreshTokenService){
        this.userService=userService;
        this.jwtService=jwtService;
        this.refreshTokenService=refreshTokenService;
    }
    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody RegisterRequest request){
        User user=userService.createUser((request));
        UserResponse response=new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getRole()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        User user = userService.authenticate(request);
        String accessToken = jwtService.generateAccessToken(user.getUsername(),user.getRole().name());
        String refreshToken = refreshTokenService.createRefreshToken(user.getUsername());

        return ResponseEntity.ok(new LoginResponse(accessToken, refreshToken));
    }
    //refresh token endpoint
    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(
            @RequestBody RefreshTokenRequest request) {

        RefreshToken storedToken =
                refreshTokenService.validateRefreshToken(
                        request.getRefreshToken()
                );

        User user =
                userService.getUserByUsername(
                        storedToken.getUsername()
                );

        storedToken.setRevoked(true);

        String newAccessToken =
                jwtService.generateAccessToken(
                        user.getUsername(),
                        user.getRole().name()
                );

        String newRefreshToken =
                refreshTokenService.createRefreshToken(
                        user.getUsername()
                );

        refreshTokenService.save(storedToken);

        return ResponseEntity.ok(
                new LoginResponse(
                        newAccessToken,
                        newRefreshToken
                )
        );
    }
}

//what payload contains
//{
//        "sub": "Rahul",
//        "iat": 1790518691,
//        "exp": 1790519591
//        }


//What you'll get now
//
//Restart the application and login again.
//        {
//        "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
//        "refreshToken": "eyJhbGciOiJIUzI1NiJ9..."}
//
//And your database should automatically have: id | token_id | username | expires_at | revoked   with Rahul | false;
