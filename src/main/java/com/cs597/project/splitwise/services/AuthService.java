package com.cs597.project.splitwise.services;

import com.cs597.project.splitwise.dto.LoginDTO;
import com.cs597.project.splitwise.dto.SignUpDTO;
import com.cs597.project.splitwise.dto.UserDTO;

public interface AuthService {
    public UserDTO signup(SignUpDTO userDTO);

    String login(LoginDTO loginDTO);
}
