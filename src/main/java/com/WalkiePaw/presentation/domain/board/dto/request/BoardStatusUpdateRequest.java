package com.WalkiePaw.presentation.domain.board.dto.request;

import com.WalkiePaw.domain.board.entity.BoardStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record BoardStatusUpdateRequest(
        @Positive Long boardId,
        @NotNull BoardStatus status) {
}
