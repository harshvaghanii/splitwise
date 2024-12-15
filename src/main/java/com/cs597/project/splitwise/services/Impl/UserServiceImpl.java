package com.cs597.project.splitwise.services.Impl;

import com.cs597.project.splitwise.dto.UserDTO;
import com.cs597.project.splitwise.entities.UserEntity;
import com.cs597.project.splitwise.exceptions.ResourceNotFoundException;
import com.cs597.project.splitwise.exceptions.UnauthorizedActionException;
import com.cs597.project.splitwise.repositories.UserRepository;
import com.cs597.project.splitwise.services.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new BadCredentialsException("User with email " + username + " not found!"));
    }

    public UserEntity getUserById(Long userID) {
        return userRepository.findById(userID)
                .orElse(null);
    }

    @Override
    public Optional<UserDTO> getUserByEmail(String email) {
        Optional<UserEntity> userEntity = userRepository.findByEmail(email);
        if (userEntity.isEmpty()) return Optional.empty();
        return Optional.of(modelMapper.map(userEntity, UserDTO.class));
    }

    @Override
    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElse(null);
    }

    @Override
    public boolean deleteUser(Long id) {
        boolean exists = userExistsByID(id);
        if (!exists) return false;
        userRepository.deleteById(id);
        return true;
    }

    @Override
    public UserDTO updateUserById(UserDTO userDTO, Long userId) {

        if (!isAuthorized(userDTO)) {
            throw new UnauthorizedActionException("You are not authorized to do that!");
        }

        if (!userExistsByID(userId)) return null;
        UserEntity userEntity = modelMapper.map(userDTO, UserEntity.class);


        userEntity.setId(userId);
        UserEntity savedEntity = userRepository.save(userEntity);
        System.out.println("In the UserService IMPL update method!");
        return modelMapper.map(savedEntity, UserDTO.class);
    }

    @Override
    public UserDTO updatePartialUserById(Long userId, Map<String, Object> updates) {
        boolean exists = userExistsByID(userId);
        if (!exists) return null;
        UserEntity entity = userRepository.findById(userId).get();
        if (!isAuthorized(modelMapper.map(entity, UserDTO.class))) {
            throw new UnauthorizedActionException("You are not authorized to do this!");
        }
        updates.forEach((field, value) -> {
            Field fieldToBeUpdated = ReflectionUtils.findRequiredField(UserEntity.class, field);
            fieldToBeUpdated.setAccessible(true);
            ReflectionUtils.setField(fieldToBeUpdated, entity, value);
        });
        entity.setUpdatedAt(LocalDateTime.now());
        return modelMapper.map(userRepository.save(entity), UserDTO.class);
    }

    @Override
    public UserEntity save(UserEntity newUser) {
        return userRepository.save(newUser);
    }

    public boolean userExistsByID(Long id) {
        return userRepository.existsById(id);
    }

    // Util method to check authorisation
    public boolean isAuthorized(UserDTO user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long requesterId = ((UserEntity) authentication.getPrincipal()).getId();
        Long editorId = user.getId();
        return requesterId.equals(editorId);
    }

}
