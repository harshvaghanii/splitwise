package com.cs597.project.splitwise.services.Impl;

import com.cs597.project.splitwise.dto.LoginDTO;
import com.cs597.project.splitwise.dto.LoginResponseDTO;
import com.cs597.project.splitwise.dto.SignUpDTO;
import com.cs597.project.splitwise.dto.UserDTO;
import com.cs597.project.splitwise.entities.UserEntity;
import com.cs597.project.splitwise.exceptions.DuplicateResourceException;
import com.cs597.project.splitwise.exceptions.ResourceNotFoundException;
import com.cs597.project.splitwise.repositories.UserRepository;
import com.cs597.project.splitwise.services.AuthService;
import com.cs597.project.splitwise.services.JWTService;
import com.cs597.project.splitwise.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final UserService userService;

    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver handlerExceptionResolver;

    @Override
    public UserDTO signup(SignUpDTO signUpDTO) {
        if (doesExist(signUpDTO.getEmail())) {
            throw new DuplicateResourceException("User with email " + signUpDTO.getEmail() + " already exists!");
        }
        UserEntity toBeCreatedUser = modelMapper.map(signUpDTO, UserEntity.class);
        String plainPassword = toBeCreatedUser.getPassword();
        String hashedPassword = passwordEncoder.encode(plainPassword);
        toBeCreatedUser.setPassword(hashedPassword);
        UserEntity savedUser = userRepository.save(toBeCreatedUser);
        return modelMapper.map(savedUser, UserDTO.class);
    }

    @Override
    public LoginResponseDTO login(HttpServletRequest request, HttpServletResponse response, LoginDTO loginDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));
            UserEntity user = (UserEntity) authentication.getPrincipal();
            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);
            return new LoginResponseDTO(user.getId(), accessToken, refreshToken);
        } catch (AuthenticationException exception) {
            throw new ResourceNotFoundException("Please check the credentials and try again!");
        }
    }

    @Override
    public LoginResponseDTO refreshToken(String refreshToken) {
        Long userId = jwtService.getUserIdFromToken(refreshToken);
        UserEntity userEntity = userService.getUserById(userId);
        String accessToken = jwtService.generateAccessToken(userEntity);
        return new LoginResponseDTO(userEntity.getId(), accessToken, refreshToken);
    }

    public boolean doesExist(String email) {
        Optional<UserEntity> user = userRepository.findByEmail(email);
        return user.isPresent();
    }

}
