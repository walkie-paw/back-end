package com.WalkiePaw.presentation.domain.qna.dto;

import com.WalkiePaw.domain.qna.entity.QnaStatus;
import com.WalkiePaw.presentation.domain.qna.dto.request.QnaUpdateRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

@Getter
public class QnaUpdateParam {

    private final @Positive Long memberId;
    private final @NotBlank String title;
    private final @NotBlank String content;
    private final @NotBlank String reply;
    private final @NotNull QnaStatus status;

    public QnaUpdateParam(QnaUpdateRequest request) {
        this.memberId = request.memberId();
        this.title = request.title();
        this.content = request.content();
        this.reply = request.reply();
        this.status = request.status();
    }
}
