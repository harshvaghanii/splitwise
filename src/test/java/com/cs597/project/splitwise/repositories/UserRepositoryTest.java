package com.cs597.project.splitwise.repositories;

import com.cs597.project.splitwise.entities.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindByEmail_whenEmailIsValid_thenReturnUser() {
        String[] validEmails = {"harshvaghani98@gmail.com", "harshvaghani1609@gmail.com",
                "harshvaghani@gmail.com", "jmac@gmail.com", "amc@gmail.com"};
        for (String email : validEmails) {
            Optional<UserEntity> user = userRepository.findByEmail(email);
            assert (user.isPresent());
        }
    }

    @Test
    void testFindByEmail_whenEmailIsNotFound_thenReturnEmptyOptionalObject() {
        String[] inValidEmails = {"harshvaghanis98@gmail.com", "harshsvaghani1609@gmail.com",
                "harshsvaghani@gmail.com", "jmc@gmail.com", "amac@gmail.com"};
        for (String email : inValidEmails) {
            Optional<UserEntity> user = userRepository.findByEmail(email);
            assert (user.isEmpty());
        }
    }
}