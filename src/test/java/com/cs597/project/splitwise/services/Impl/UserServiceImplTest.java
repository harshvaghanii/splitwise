package com.cs597.project.splitwise.services.Impl;

import com.cs597.project.splitwise.TestContainerConfiguration;
import com.cs597.project.splitwise.dto.UserDTO;
import com.cs597.project.splitwise.entities.UserEntity;
import com.cs597.project.splitwise.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@Import(TestContainerConfiguration.class)
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Spy
    private ModelMapper modelMapper;

    @InjectMocks
    UserServiceImpl userService;

    @Test
    void testGetUserById_WhenUserIdIsPresent_ThenReturnEmployee() {
        // Assign

        Long id = 1L;
        UserEntity mockUser = UserEntity
                .builder()
                .id(id)
                .name("Harsh Vaghani")
                .email("harsh@gmail.com")
                .password("1234")
                .build();
        when(userRepository.findById(id))
                .thenReturn(Optional.of(mockUser)); // Stubbing

        // Act
        UserEntity user = userService.getUserById(id);

        // Assert

        assertThat(user.getId()).isEqualTo(id);
        assertThat(user.getEmail()).isEqualTo(mockUser.getEmail());
        verify(userRepository, atLeast(1)).findById(id);

    }

    @Test
    void testGetUserByEmail_WhenUserEmailIsPresent_ThenReturnUserDTO() {
        String email = "harshvaghani@gmail.com";
        UserEntity mockUser = UserEntity
                .builder()
                .id(5L)
                .name("Harsh Vaghani")
                .email(email)
                .password("1234")
                .build();
        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(mockUser));

        Optional<UserDTO> userDTO = userService.getUserByEmail(email);

        assertThat(userDTO).isPresent();
        assertThat(userDTO.get().getEmail()).isEqualTo(mockUser.getEmail());
        assertThat(userDTO.get().getId()).isEqualTo(mockUser.getId());
        verify(userRepository, atLeast(1)).findByEmail(email);
    }

}