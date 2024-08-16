package com.WalkiePaw.presentation.domain.review.dto.request;

import com.WalkiePaw.domain.board.entity.BoardCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ReviewSaveRequest(
        @Positive int point,
        @NotBlank String content,
        @Positive Long chatroomId,
        @Positive Long reviewerId,
        @NotNull BoardCategory category) {
}
