package com.example.demo.service;

import com.example.demo.exception.InvalidRefreshTokenException;
import com.example.demo.model.RefreshToken;
import com.example.demo.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

    public RefreshTokenService(
            RefreshTokenRepository refreshTokenRepository,
            JwtService jwtService) {

        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtService = jwtService;
    }

    public String createRefreshToken(String username) {

        String tokenId = UUID.randomUUID().toString();

        String refreshToken =
                jwtService.generateRefreshToken(
                        username,
                        tokenId
                );

        RefreshToken refreshTokenEntity = new RefreshToken();

        refreshTokenEntity.setTokenId(tokenId);
        refreshTokenEntity.setUsername(username);
        refreshTokenEntity.setExpiresAt(
                LocalDateTime.now().plusDays(7)
        );
        refreshTokenEntity.setRevoked(false);

        refreshTokenRepository.save(refreshTokenEntity);

        return refreshToken;
    }
    //below methods are refreshtoken rotation:
    public RefreshToken validateRefreshToken(String token) {

        Claims claims;
        try {
            claims = jwtService.parseToken(token);
        } catch (Exception ex) {
            throw new InvalidRefreshTokenException(
                    "Invalid or expired refresh token"
            );
        }

        if (!jwtService.isRefreshToken(claims)) {
            throw new InvalidRefreshTokenException("Invalid refresh token");
        }

        String tokenId = claims.getId();//this is the jti you can see in your token //We use that ID to find the corresponding database record.

        RefreshToken refreshToken =
                refreshTokenRepository.findByTokenId(tokenId)
                        .orElseThrow(() ->
                                new InvalidRefreshTokenException(
                                        "Refresh token not found"
                                ));

        if (refreshToken.isRevoked()) {
            throw new InvalidRefreshTokenException(
                    "Refresh token has been revoked"
            );
        }

        if (refreshToken.getExpiresAt()
                .isBefore(LocalDateTime.now())) {

            throw new InvalidRefreshTokenException(
                    "Refresh token has expired"
            );
        }

        return refreshToken;
    }
    public void save(RefreshToken refreshToken) {
        refreshTokenRepository.save(refreshToken);
    }
}