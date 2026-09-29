package com.example.demo.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey secretKey;

    public JwtService(SecretKey secretKey) {
        this.secretKey = secretKey;
    }

    public String generateAccessToken(String username, String role) {

        Instant now = Instant.now();
        Instant expiration = now.plusSeconds(15 * 60);//token expires after 15 minutes

        return Jwts.builder()
                .subject(username)
                .claim("role",role)
                .claim("type", "access")
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .signWith(secretKey)
                .compact();
    }
    public String generateRefreshToken(
            String username,
            String tokenId) {

        Instant now = Instant.now();
        Instant expiration = now.plusSeconds(7 * 24 * 60 * 60);

        return Jwts.builder()
                .subject(username)
                .id(tokenId)
                .claim("type", "refresh")
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .signWith(secretKey)
                .compact();
    }
    //methods below are for refresh-token rotation: every successful refresh invalidates the old refresh token.
    public Claims parseToken(String token) {

        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    public boolean isRefreshToken(Claims claims) {

        return "refresh".equals(
                claims.get("type", String.class)
        );
    }

}

//acess needs role
//refresh token doesnt need role