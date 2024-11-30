package com.cs597.project.splitwise.repositories;

import com.cs597.project.splitwise.entities.TransactionEntity;
import com.cs597.project.splitwise.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    public Optional<UserEntity> findByEmail(String email);
}
