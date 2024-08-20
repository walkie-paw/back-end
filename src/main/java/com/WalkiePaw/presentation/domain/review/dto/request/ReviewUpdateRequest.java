package com.WalkiePaw.presentation.domain.review.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public record ReviewUpdateRequest(
        @NotBlank String content,
        @PositiveOrZero int point) {
}
