package com.cs597.project.splitwise.repositories;

import com.cs597.project.splitwise.TestContainerConfiguration;
import com.cs597.project.splitwise.entities.UserEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Import(TestContainerConfiguration.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void addUsersInDatabase() {
        // Adding dummy users in the database
        List<UserEntity> listOfDummyUsers = new ArrayList<>();
        UserEntity user1 = UserEntity
                .builder()
                .name("Harsh Vaghani")
                .password("1234")
                .email("harshvaghani99@gmail.com")
                .build();
        listOfDummyUsers.add(user1);
        for (UserEntity user : listOfDummyUsers) {
            userRepository.save(user);
        }
    }

    @AfterEach
    public void removeUsersInDatabase() {
        userRepository.deleteAll();
    }

    @Test
    void testFindByEmail_whenEmailIsValid_thenReturnUser() {
        String[] validEmails = {"harshvaghani99@gmail.com"};
        for (String email : validEmails) {
            Optional<UserEntity> user = userRepository.findByEmail(email);
            assert (user.isPresent());
            assert (user.get().getEmail().equals(email));
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