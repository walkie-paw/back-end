package com.WalkiePaw.presentation.domain.review.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record ReviewUpdateRequest(
        @NotBlank String content,
        @Positive int point) {
}
