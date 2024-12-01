package com.cs597.project.splitwise.services;

import com.cs597.project.splitwise.entities.UserEntity;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {
    public UserEntity getUserById(Long userId);
}
