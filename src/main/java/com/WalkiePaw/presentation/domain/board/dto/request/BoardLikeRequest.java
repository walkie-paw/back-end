package com.WalkiePaw.presentation.domain.board.dto.request;

import jakarta.validation.constraints.Positive;

public record BoardLikeRequest(
        @Positive Long boardId,
        @Positive Long loginUserId) {
}
