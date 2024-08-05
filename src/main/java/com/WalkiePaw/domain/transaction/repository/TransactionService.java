package com.WalkiePaw.domain.transaction.repository;

import com.WalkiePaw.domain.transaction.service.TransactionRepository;
import com.WalkiePaw.presentation.domain.chatroom.response.TransactionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;

//    public Page<TransactionResponse> findTransaction(final Long memberId, final Pageable pageable) {
//        return transactionRepository.findByMemberId(memberId, pageable);
//    }
}
