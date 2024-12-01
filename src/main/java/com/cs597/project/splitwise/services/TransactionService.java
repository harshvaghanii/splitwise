package com.cs597.project.splitwise.services;

import com.cs597.project.splitwise.dto.TransactionDTO;

import java.util.List;

public interface TransactionService {
    public List<TransactionDTO> getAllTransactionsByUserId(Long userId);

    public TransactionDTO addTransaction(TransactionDTO transactionDTO);
}
