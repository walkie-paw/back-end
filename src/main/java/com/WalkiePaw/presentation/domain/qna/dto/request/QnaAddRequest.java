package com.WalkiePaw.presentation.domain.qna.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record QnaAddRequest(
        @Positive Long memberId,
        @NotBlank String title,
        @NotBlank String content) {
}
