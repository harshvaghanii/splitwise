package com.cs597.project.splitwise.controllers;

import com.cs597.project.splitwise.dto.SignUpDTO;
import com.cs597.project.splitwise.dto.UserDTO;
import com.cs597.project.splitwise.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/user/auth/")
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;

    @PostMapping(path = "signup")
    public UserDTO signup(@RequestBody SignUpDTO signUpDTO) {
        return authService.signup(signUpDTO);
    }

}
