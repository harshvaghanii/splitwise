package com.cs597.project.splitwise.services.Impl;

import com.cs597.project.splitwise.entities.UserEntity;
import com.cs597.project.splitwise.services.JWTService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JWTServiceImpl implements JWTService {

    @Value("${jwt.secretKey}")
    private String jwtSecretKey;

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String generateAccessToken(UserEntity userEntity) {
        return Jwts.builder()
                .subject(String.valueOf(userEntity.getId()))
                .claim("email", userEntity.getEmail())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + (1000 * 60 * 10)))
                .signWith(getSecretKey())
                .compact();
    }

    @Override
    public String generateRefreshToken(UserEntity userEntity) {
        return Jwts.builder()
                .subject(String.valueOf(userEntity.getId()))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 7)))
                .signWith(getSecretKey())
                .compact();
    }

    @Override
    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return Long.valueOf(claims.getSubject());
    }
}
