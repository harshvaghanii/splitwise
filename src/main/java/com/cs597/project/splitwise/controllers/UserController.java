package com.cs597.project.splitwise.controllers;

import com.cs597.project.splitwise.advices.ApiResponse;
import com.cs597.project.splitwise.dto.UserDTO;
import com.cs597.project.splitwise.entities.UserEntity;
import com.cs597.project.splitwise.exceptions.ResourceNotFoundException;
import com.cs597.project.splitwise.exceptions.UnauthorizedActionException;
import com.cs597.project.splitwise.services.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final ModelMapper modelMapper;

    @GetMapping(path = "/id/{userID}")
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(@PathVariable Long userID) {
        UserEntity userEntity = userService.getUserById(userID);
        if (userEntity == null) {
            throw new ResourceNotFoundException("Employee with id " + userID + " not found!");
        }
        UserDTO user = modelMapper.map(userEntity, UserDTO.class);
        return new ResponseEntity<>(new ApiResponse<>(user), HttpStatus.OK);
    }

    @GetMapping(path = "/email/{email}")
    public ResponseEntity<ApiResponse<UserDTO>> getUserByEmail(@PathVariable String email) {
        Optional<UserDTO> user = userService.getUserByEmail(email);
        if (user.isEmpty()) {
            throw new ResourceNotFoundException("User with email " + email + " not found!");
        }
        return new ResponseEntity<>(new ApiResponse<>(user.get()), HttpStatus.OK);
    }

    @PutMapping(path = "/{userId}")
    public ResponseEntity<ApiResponse<UserDTO>> updateEmployeeById(@RequestBody UserDTO userDTO, @PathVariable Long userId) {
        try {
            UserDTO userDTO1 = userService.updateUserById(userDTO, userId);
            if (userDTO1 == null) {
                throw new ResourceNotFoundException("Employee with id " + userId + " not found!");
            }
            return new ResponseEntity<>(new ApiResponse<>(userDTO1), HttpStatus.OK);
        } catch (UnauthorizedActionException exception) {
            throw new UnauthorizedActionException(exception.getMessage());
        }
    }

    @PatchMapping(path = "/{userId}")
    public ResponseEntity<ApiResponse<UserDTO>> updatePartialEmployeeById(@PathVariable Long userId, @RequestBody Map<String, Object> updates) {
        try {
            UserDTO userDTO = userService.updatePartialUserById(userId, updates);
            if (userDTO == null) {
                throw new ResourceNotFoundException("Employee with id " + userId + " not found!");
            }
            return new ResponseEntity<>(new ApiResponse<>(userDTO), HttpStatus.OK);
        } catch (ResourceNotFoundException exception) {
            throw new ResourceNotFoundException(exception.getMessage());
        }

    }

    @DeleteMapping(path = "/{userId}")
    public ResponseEntity<ApiResponse<Boolean>> deleteEmployee(@PathVariable Long userId) {
        boolean deleted = userService.deleteUser(userId);
        if (!deleted) {
            throw new ResourceNotFoundException("Employee with id " + userId + " not found!");
        }
        return new ResponseEntity<>(new ApiResponse<>(deleted), HttpStatus.OK);

    }

}
