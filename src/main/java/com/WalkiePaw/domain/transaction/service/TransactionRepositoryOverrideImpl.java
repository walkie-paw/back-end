package com.WalkiePaw.domain.transaction.service;

import com.WalkiePaw.domain.transaction.entity.Transaction;
import com.WalkiePaw.global.util.Querydsl4RepositorySupport;
import org.springframework.stereotype.Repository;

@Repository
public class TransactionRepositoryOverrideImpl extends Querydsl4RepositorySupport implements TransactionRepositoryOverride {

    public TransactionRepositoryOverrideImpl() {
        super(Transaction.class);
    }


//    @Override
//    public Page<TransactionResponse> findTransaction(final Long memberId, Pageable pageable) {
//
//    }
}
