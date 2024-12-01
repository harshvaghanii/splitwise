package com.cs597.project.splitwise.controllers;

import com.cs597.project.splitwise.advices.ApiResponse;
import com.cs597.project.splitwise.dto.TransactionDTO;
import com.cs597.project.splitwise.exceptions.ResourceNotFoundException;
import com.cs597.project.splitwise.services.TransactionService;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Data
@RequestMapping(path = "/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<ApiResponse<TransactionDTO>> addTransaction(@RequestBody TransactionDTO transactionDTO) {
        try {
            TransactionDTO transaction = transactionService.addTransaction(transactionDTO);
            return new ResponseEntity<>(new ApiResponse<>(transaction), HttpStatus.CREATED);
        } catch (ResourceNotFoundException exception) {
            throw new ResourceNotFoundException(exception.getMessage());
        }
    }

}


