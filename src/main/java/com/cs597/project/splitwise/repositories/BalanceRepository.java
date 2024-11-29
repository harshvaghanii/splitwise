package com.cs597.project.splitwise.repositories;

import com.cs597.project.splitwise.entities.BalanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BalanceRepository extends JpaRepository<BalanceEntity, Long> {
}
