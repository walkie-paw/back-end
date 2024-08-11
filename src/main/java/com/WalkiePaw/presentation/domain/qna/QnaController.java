package com.WalkiePaw.presentation.domain.qna;

import com.WalkiePaw.domain.qna.service.QnaService;
import com.WalkiePaw.presentation.domain.qna.response.*;
import com.WalkiePaw.presentation.domain.qna.request.QnaAddRequest;
import com.WalkiePaw.presentation.domain.qna.request.QnaUpdateRequest;
import com.WalkiePaw.presentation.domain.qna.request.ReplyUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/qna")
@RequiredArgsConstructor
public class QnaController {

    private final QnaService qnaService;
    private static final String QNA_URL = "/qna/";

    @GetMapping
    public ResponseEntity<Page<QnaListResponse>> qnaList(final Pageable pageable) {
        Page<QnaListResponse> responses = qnaService.findAll(pageable);
        return ResponseEntity.ok()
                .body(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QnaGetResponse> getQna(@PathVariable("id") final Long qnaId) {
        QnaGetResponse qnaGetResponse = qnaService.findById(qnaId);
        return ResponseEntity.ok()
                .body(qnaGetResponse);
    }

    @PostMapping
    public ResponseEntity<Void> addQna(@Validated @RequestBody final QnaAddRequest request) {
        Long qnaId = qnaService.save(request);
        return ResponseEntity.created(URI.create(QNA_URL + qnaId)).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateQna(@PathVariable("id") final Long qnaId, @Validated @RequestBody final QnaUpdateRequest request) {
        qnaService.update(qnaId, request);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/reply")
    public ResponseEntity<Void> replyQna(@PathVariable("id") final Long qnaId, @Validated @RequestBody final ReplyUpdateRequest request) {
        qnaService.updateReply(qnaId, request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/list")
    public ResponseEntity<Page<QnaListResponse>> list(
            @RequestParam(required = false) final String status, // RESOLVED, UNRESOLVED
            Pageable pageable
    ) {
        Page<QnaListResponse> list = qnaService.findAllByCond(status, pageable);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}/list")
    public ResponseEntity<Page<QnaListResponse>> mypageList(
            @PathVariable("id") final Long memberId,
            Pageable pageable) {
        Page<QnaListResponse> list = qnaService.findByMemberId(memberId, pageable);
        return ResponseEntity.ok(list);
    }

}
