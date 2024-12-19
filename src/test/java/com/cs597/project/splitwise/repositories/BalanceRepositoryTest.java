package com.cs597.project.splitwise.repositories;

import com.cs597.project.splitwise.entities.BalanceEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
class BalanceRepositoryTest {

    @Autowired
    private BalanceRepository balanceRepository;

    @Test
    void testFindByBalanceId_whenIdIsValid_thenReturnBalance() {
        String[] validBalanceId = {"1_2", "1_3", "3_4", "1_4"};
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