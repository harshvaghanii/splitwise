package com.cs597.project.splitwise.services.Impl;

import com.cs597.project.splitwise.entities.UserEntity;
import com.cs597.project.splitwise.exceptions.ResourceNotFoundException;
import com.cs597.project.splitwise.repositories.UserRepository;
import com.cs597.project.splitwise.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new BadCredentialsException("User with email " + username + " not found!"));
    }

    public UserEntity getUserById(Long userID) {
        return userRepository.findById(userID)
                .orElseThrow(() -> new BadCredentialsException("User with id " + userID + " not found!"));
    }
}
