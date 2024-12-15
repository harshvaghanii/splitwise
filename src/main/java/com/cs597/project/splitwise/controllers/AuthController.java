package com.cs597.project.splitwise.controllers;

import com.cs597.project.splitwise.advices.ApiResponse;
import com.cs597.project.splitwise.dto.LoginDTO;
import com.cs597.project.splitwise.dto.LoginResponseDTO;
import com.cs597.project.splitwise.dto.SignUpDTO;
import com.cs597.project.splitwise.dto.UserDTO;
import com.cs597.project.splitwise.services.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RequestMapping("/user/auth/")
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;
    @Value("${deploy.env}")
    private String environment;

    @GetMapping(path = "/some")
    public ResponseEntity<ApiResponse<String>> signup() {
        String message = "Reached here!";
        if (message == null) return ResponseEntity.notFound().build();
        return new ResponseEntity<>(new ApiResponse<>(message), HttpStatus.OK);
    }

    @PostMapping(path = "signup")
    public ResponseEntity<ApiResponse<UserDTO>> signup(@RequestBody SignUpDTO signUpDTO) {
        UserDTO userDTO = authService.signup(signUpDTO);
        return new ResponseEntity<>(new ApiResponse<>(userDTO), HttpStatus.CREATED);
    }

    @PostMapping(path = "login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(@RequestBody LoginDTO loginDTO, HttpServletRequest request,
                                                               HttpServletResponse response) {
        LoginResponseDTO loginResponseDTO = authService.login(request, response, loginDTO);
        String accessToken = loginResponseDTO.getAccessToken();
        String refreshToken = loginResponseDTO.getRefreshToken();
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure("production".equals(environment));
        response.addCookie(cookie);
        return new ResponseEntity<>(new ApiResponse<>(loginResponseDTO), HttpStatus.OK);
    }

    @PostMapping(path = "refresh")
    public ResponseEntity<LoginResponseDTO> refresh(HttpServletRequest request) {
        String refreshToken = Arrays.stream(request.getCookies())
                .filter(cookie -> "refreshToken".equals(cookie.getName())).
                findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new AuthenticationServiceException("No refresh token found in the Cookies!"));
        LoginResponseDTO loginResponseDTO = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(loginResponseDTO);
    }

}
