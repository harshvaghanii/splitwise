package com.cs597.project.splitwise.repositories;

import com.cs597.project.splitwise.entities.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
}
