package com.WalkiePaw.presentation.domain.transaction;

import com.WalkiePaw.domain.transaction.repository.TransactionService;
import com.WalkiePaw.presentation.domain.chatroom.response.TransactionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/transaction")
public class TransactionController {
    private final TransactionService transactionService;
//    @GetMapping
//    public ResponseEntity<Page<TransactionResponse>> getTransaction(final @PathVariable("id") Long memberId, Pageable pageable) {
//        Page<TransactionResponse> transaction = transactionService.getTransactionsByMemberId(memberId, pageable);
//        return ResponseEntity.ok(transaction);
//    }

}
