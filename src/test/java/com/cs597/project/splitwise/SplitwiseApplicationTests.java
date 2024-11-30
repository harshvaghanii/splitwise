package com.cs597.project.splitwise;

import com.cs597.project.splitwise.entities.UserEntity;
import com.cs597.project.splitwise.services.JWTService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Objects;

@SpringBootTest
class SplitwiseApplicationTests {

    @Autowired
    JWTService jwtService;

    @Test
    void contextLoads() {

        UserEntity userEntity = new UserEntity(1L, "Harsh", "harsh98@gmail.com", "1234", LocalDateTime.now(), LocalDateTime.now());
        String token = jwtService.generateToken(userEntity);
        System.out.println(token);
        Long userId = jwtService.getUserIdFromToken(token);
        System.out.println(userId);
        assert (Objects.equals(userId, userEntity.getId()));
        assert (Objects.equals(userId, userEntity.getId()));
    }

}
