package com.cs597.project.splitwise.repositories;

import com.cs597.project.splitwise.TestContainerConfiguration;
import com.cs597.project.splitwise.entities.BalanceEntity;
import com.cs597.project.splitwise.entities.UserEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.Optional;

@DataJpaTest
@Import(TestContainerConfiguration.class)
class BalanceRepositoryTest {

    @Autowired
    private BalanceRepository balanceRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void addBalances() {
        UserEntity user1 = UserEntity
                .builder()
                .name("Harsh")
                .email("hvagahani@gmail.com")
                .password("1234")
                .build();
        UserEntity user2 = UserEntity
                .builder()
                .name("Aby")
                .email("aby@gmail.com")
                .password("1234")
                .build();
        userRepository.save(user1);
        userRepository.save(user2);
        BalanceEntity balanceEntity = BalanceEntity
                .builder()
                .user1(user1)
                .user2(user2)
                .balanceAmount(BigDecimal.TEN)
                .balanceId("1_2")
                .build();
        balanceRepository.save(balanceEntity);
    }

    @AfterEach
    public void clearDB() {
        balanceRepository.deleteAll();
    }

    @Test
    void testFindByBalanceId_whenIdIsValid_thenReturnBalance() {
        String[] validBalanceId = {"1_2"};
        for (String balanceId : validBalanceId) {
            Optional<BalanceEntity> balance = balanceRepository.findByBalanceId(balanceId);
            assert (balance.isPresent());
        }
    }

    @Test
    void testFindByBalanceId_whenIdIsInValid_thenReturnEmptyOptionalObject() {
        String[] validBalanceId = {"2_1", "3_1", "4_3", "2_3", "1_1"};
        for (String balanceId : validBalanceId) {
            Optional<BalanceEntity> balance = balanceRepository.findByBalanceId(balanceId);
            assert (balance.isEmpty());
        }
    }
}