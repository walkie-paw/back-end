package com.WalkiePaw.presentation.domain.qna;

import com.WalkiePaw.domain.qna.service.QnaService;
import com.WalkiePaw.presentation.domain.qna.dto.QnaAddParam;
import com.WalkiePaw.presentation.domain.qna.dto.QnaUpdateParam;
import com.WalkiePaw.presentation.domain.qna.dto.request.QnaAddRequest;
import com.WalkiePaw.presentation.domain.qna.dto.request.QnaUpdateRequest;
import com.WalkiePaw.presentation.domain.qna.dto.request.ReplyUpdateRequest;
import com.WalkiePaw.presentation.domain.qna.dto.response.QnaGetResponse;
import com.WalkiePaw.presentation.domain.qna.dto.response.QnaListResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/v1/qna")
@RequiredArgsConstructor
public class QnaController {

    private final QnaService qnaService;
    private static final String QNA_URL = "/api/v1/qna/";

    @GetMapping
    @ResponseStatus(OK)
    public Page<QnaListResponse> qnaList(final Pageable pageable) {
        return qnaService.findAll(pageable);
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    public QnaGetResponse getQna(final @PathVariable("id") @Positive Long qnaId) {
        return qnaService.findById(qnaId);
    }

    @PostMapping
    public ResponseEntity<Void> addQna(final @RequestBody @Validated QnaAddRequest request) {
        QnaAddParam param = new QnaAddParam(request);
        Long qnaId = qnaService.save(param);
        return ResponseEntity.created(URI.create(QNA_URL + qnaId)).build();
    }

    @PatchMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void updateQna(final @PathVariable("id") @Positive Long qnaId, final @RequestBody @Validated QnaUpdateRequest request) {
        QnaUpdateParam param = new QnaUpdateParam(request);
        qnaService.update(qnaId, param);
    }

    @PatchMapping("/{id}/reply")
    @ResponseStatus(NO_CONTENT)
    public void replyQna(final @PathVariable("id") @Positive Long qnaId, final @RequestBody @Validated ReplyUpdateRequest request) {
        qnaService.updateReply(qnaId, request.reply());
    }

    @GetMapping("/list")
    @ResponseStatus(OK)
    public Page<QnaListResponse> list(
            final @RequestParam(required = false) @NotBlank String status, // RESOLVED, UNRESOLVED
            Pageable pageable
    ) {
        return qnaService.findAllByCond(status, pageable);
    }

    @GetMapping("/{id}/list")
    @ResponseStatus(OK)
    public Page<QnaListResponse> mypageList(
            final @PathVariable("id") @Positive Long memberId,
            Pageable pageable) {
        return qnaService.findByMemberId(memberId, pageable);
    }

}
