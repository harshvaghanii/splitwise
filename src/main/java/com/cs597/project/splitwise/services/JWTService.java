package com.cs597.project.splitwise.services;

import com.cs597.project.splitwise.entities.UserEntity;

public interface JWTService {
    public String generateToken(UserEntity userEntity);

    public Long getUserIdFromToken(String token);
}
