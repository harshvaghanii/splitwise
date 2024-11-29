package com.cs597.project.splitwise.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BalanceDTO {
    private UserDTO user1;
    private UserDTO user2;
    private BigDecimal balanceAmount;
    private LocalDateTime lastUpdated;
}
