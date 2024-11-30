package com.cs597.project.splitwise.services.Impl;

import com.cs597.project.splitwise.dto.LoginDTO;
import com.cs597.project.splitwise.dto.SignUpDTO;
import com.cs597.project.splitwise.dto.UserDTO;
import com.cs597.project.splitwise.entities.UserEntity;
import com.cs597.project.splitwise.repositories.UserRepository;
import com.cs597.project.splitwise.services.AuthService;
import com.cs597.project.splitwise.services.JWTService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    @Override
    public UserDTO signup(SignUpDTO signUpDTO) {
        if (doesExist(signUpDTO.getEmail())) {
            throw new RuntimeException("User with email " + signUpDTO.getEmail() + " already exists!");
        }
        UserEntity toBeCreatedUser = modelMapper.map(signUpDTO, UserEntity.class);
        String plainPassword = toBeCreatedUser.getPassword();
        String hashedPassword = passwordEncoder.encode(plainPassword);
        toBeCreatedUser.setPassword(hashedPassword);
        UserEntity savedUser = userRepository.save(toBeCreatedUser);
        return modelMapper.map(savedUser, UserDTO.class);
    }

    @Override
    public String login(LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword())
        );
        UserEntity user = (UserEntity) authentication.getPrincipal();
        return jwtService.generateToken(user);
    }

    public boolean doesExist(String email) {
        Optional<UserEntity> user = userRepository.findByEmail(email);
        return user.isPresent();
    }

}
