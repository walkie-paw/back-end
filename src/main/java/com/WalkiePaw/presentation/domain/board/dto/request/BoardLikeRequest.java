package com.WalkiePaw.presentation.domain.board.dto.request;

import lombok.Data;

@Data
public class BoardLikeRequest {
    private final Long boardId;
    private final Long loginUserId;
}
