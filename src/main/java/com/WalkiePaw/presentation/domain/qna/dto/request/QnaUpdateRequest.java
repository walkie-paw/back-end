package com.WalkiePaw.presentation.domain.qna.dto.request;

import com.WalkiePaw.domain.qna.entity.QnaStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record QnaUpdateRequest(
        @Positive Long memberId,
        @NotBlank String title,
        @NotBlank String content,
        @NotBlank String reply,
        @NotNull QnaStatus status) {
}
