package com.WalkiePaw.domain.transaction.service;

import com.WalkiePaw.domain.transaction.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long>, TransactionRepositoryOverride {
}
