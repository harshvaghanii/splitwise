package com.cs597.project.splitwise.services.Impl;

import com.cs597.project.splitwise.dto.SignUpDTO;
import com.cs597.project.splitwise.dto.UserDTO;
import com.cs597.project.splitwise.entities.UserEntity;
import com.cs597.project.splitwise.repositories.UserRepository;
import com.cs597.project.splitwise.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserDTO signup(SignUpDTO signUpDTO) {
        if (doesExist(signUpDTO)) {
            throw new RuntimeException("User with email " + signUpDTO.getEmail() + " already exists!");
        }
        UserEntity toBeCreatedUser = modelMapper.map(signUpDTO, UserEntity.class);
        UserEntity savedUser = userRepository.save(toBeCreatedUser);
        return modelMapper.map(savedUser, UserDTO.class);
    }

    public boolean doesExist(SignUpDTO signUpDTO) {
        Optional<UserEntity> user = userRepository.findByEmail(signUpDTO.getEmail());
        return user.isPresent();
    }

}
