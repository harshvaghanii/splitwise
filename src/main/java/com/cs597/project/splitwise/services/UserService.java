package com.cs597.project.splitwise.services;

import com.cs597.project.splitwise.dto.UserDTO;
import com.cs597.project.splitwise.entities.UserEntity;

import java.util.Map;
import java.util.Optional;

public interface UserService {
    public UserEntity getUserById(Long userId);

    Optional<UserDTO> getUserByEmail(String email);

    public boolean deleteUser(Long id);

    public UserDTO updateUserById(UserDTO userDTO, Long id);

    public UserDTO updatePartialUserById(Long userId, Map<String, Object> updates);

    UserEntity save(UserEntity newUser);
}
