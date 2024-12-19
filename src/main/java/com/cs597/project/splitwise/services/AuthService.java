package com.cs597.project.splitwise.services;

import com.cs597.project.splitwise.dto.LoginDTO;
import com.cs597.project.splitwise.dto.LoginResponseDTO;
import com.cs597.project.splitwise.dto.SignUpDTO;
import com.cs597.project.splitwise.dto.UserDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    public UserDTO signup(SignUpDTO userDTO);

    LoginResponseDTO login(HttpServletRequest request, HttpServletResponse response, LoginDTO loginDTO);

    LoginResponseDTO refreshToken(String refreshToken);
}
