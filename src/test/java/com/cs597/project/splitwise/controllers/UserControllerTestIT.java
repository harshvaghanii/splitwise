package com.cs597.project.splitwise.controllers;

import com.cs597.project.splitwise.TestContainerConfiguration;
import com.cs597.project.splitwise.advices.ApiResponse;
import com.cs597.project.splitwise.dto.UserDTO;
import com.cs597.project.splitwise.entities.UserEntity;
import com.cs597.project.splitwise.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestContainerConfiguration.class)
@AutoConfigureWebTestClient(timeout = "100000")
class UserControllerTestIT {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UserRepository userRepository;

    private UserEntity userEntity;
    private UserDTO userDTO;

    @BeforeEach
    public void setup() {
        userEntity = UserEntity
                .builder()
                .email("harsh@gmail.com")
                .name("Harsh Vaghani")
                .password("1234")
                .build();
        userDTO = UserDTO
                .builder()
                .name("Harsh Vaghani")
                .email("harsh@gmail.com")
                .build();
    }

    @Test
    public void testGetUserById_success() {
        // Save the user entity
        UserEntity user = userRepository.save(userEntity);

        // Define the parameterized response type for ApiResponse<UserDTO>
        ParameterizedTypeReference<ApiResponse<UserDTO>> responseType =
                new ParameterizedTypeReference<ApiResponse<UserDTO>>() {
                };

        // Perform the GET request and validate the response
        webTestClient.get()
                .uri("/user/id/{userId}", user.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(responseType)
                .consumeWith(response -> {
                    // Deserialize the response body
                    ApiResponse<UserDTO> apiResponse = response.getResponseBody();
                    assert apiResponse != null;

                    // Validate the UserDTO data
                    UserDTO actualUserDTO = apiResponse.getData();
                    assert actualUserDTO != null;
                    assert actualUserDTO.getName().equals(userDTO.getName());
                    assert actualUserDTO.getEmail().equals(userDTO.getEmail());
                });
    }


}