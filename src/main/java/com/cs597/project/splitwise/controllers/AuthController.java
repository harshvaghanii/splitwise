package com.cs597.project.splitwise.controllers;

import com.cs597.project.splitwise.dto.LoginDTO;
import com.cs597.project.splitwise.dto.SignUpDTO;
import com.cs597.project.splitwise.dto.UserDTO;
import com.cs597.project.splitwise.services.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/user/auth/")
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;

    @GetMapping(path = "signup")
    public String signup() {
        return "Reached here!";
    }

    @PostMapping(path = "signup")
    public UserDTO signup(@RequestBody SignUpDTO signUpDTO) {
        return authService.signup(signUpDTO);
    }

    @PostMapping(path = "login")
    public String login(@RequestBody LoginDTO loginDTO, HttpServletRequest request,
                        HttpServletResponse response) {
        String token = authService.login(loginDTO);
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return token;
    }

}
