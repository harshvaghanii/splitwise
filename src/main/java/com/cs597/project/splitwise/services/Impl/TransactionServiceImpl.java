package com.cs597.project.splitwise.services.Impl;

import com.cs597.project.splitwise.dto.TransactionDTO;
import com.cs597.project.splitwise.dto.UserDTO;
import com.cs597.project.splitwise.entities.BalanceEntity;
import com.cs597.project.splitwise.entities.TransactionEntity;
import com.cs597.project.splitwise.entities.UserEntity;
import com.cs597.project.splitwise.exceptions.ResourceNotFoundException;
import com.cs597.project.splitwise.exceptions.UnauthorizedActionException;
import com.cs597.project.splitwise.repositories.BalanceRepository;
import com.cs597.project.splitwise.repositories.TransactionRepository;
import com.cs597.project.splitwise.repositories.UserRepository;
import com.cs597.project.splitwise.services.TransactionService;
import com.cs597.project.splitwise.utilities.BalanceUtility;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Data
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final BalanceRepository balanceRepository;
    private final JWTServiceImpl jwtService;

    @Override
    public List<TransactionDTO> getAllTransactionsByUserId(Long userId) {
        return List.of();
    }

    @Override
    public TransactionDTO addTransaction(TransactionDTO transactionDTO) {

        // Authorizing the requests
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long requesterId = ((UserEntity) authentication.getPrincipal()).getId();

        Long paidById = transactionDTO.getPaidBy().getId();
        Long owedById = transactionDTO.getOwedBy().getId();

        if (!requesterId.equals(paidById) && !requesterId.equals(owedById)) {
            throw new UnauthorizedActionException("You are not authorized to perform this action.");
        }

        Long ID = transactionDTO.getPaidBy().getId();
        UserEntity paidBy = userRepository.findById(transactionDTO.getPaidBy().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + transactionDTO.getPaidBy().getId()));

        UserEntity owedBy = userRepository.findById(transactionDTO.getOwedBy().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + transactionDTO.getOwedBy().getId()));
        TransactionEntity toSave = modelMapper.map(transactionDTO, TransactionEntity.class);
        toSave.setPaidBy(paidBy);
        toSave.setOwedBy(owedBy);
        TransactionEntity savedTransaction = transactionRepository.save(toSave);

        // Modify the balance table
        boolean paidByUserOne = transactionDTO.getPaidBy().getId() < transactionDTO.getOwedBy().getId();
        updateBalance(transactionDTO.getPaidBy(), transactionDTO.getOwedBy(), paidByUserOne, transactionDTO.getAmount());

        return modelMapper.map(savedTransaction, TransactionDTO.class);

    }

    public void updateBalance(UserDTO user1, UserDTO user2, boolean paidByUserOne, BigDecimal amount) {
        String balanceId = BalanceUtility.generateBalanceId(user1, user2);
        BalanceEntity balanceEntity = balanceRepository.findByBalanceId(balanceId)
                .orElseGet(() -> BalanceEntity.builder()
                        .balanceId(balanceId)
                        .balanceAmount(BigDecimal.ZERO)
                        .user1(modelMapper.map(user1, UserEntity.class))
                        .user2(modelMapper.map(user2, UserEntity.class))
                        .createdAt(LocalDateTime.now())
                        .createdAt(LocalDateTime.now())
                        .build());

        if (!paidByUserOne) {
            amount = amount.negate();
        }
        balanceEntity.setBalanceAmount(balanceEntity.getBalanceAmount().add(amount));
        balanceEntity.setLastUpdated(LocalDateTime.now());

        balanceRepository.save(balanceEntity);
    }
}
