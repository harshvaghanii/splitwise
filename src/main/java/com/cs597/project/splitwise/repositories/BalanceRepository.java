package com.cs597.project.splitwise.repositories;

import com.cs597.project.splitwise.entities.BalanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BalanceRepository extends JpaRepository<BalanceEntity, Long> {
}
