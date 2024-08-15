package com.WalkiePaw.presentation.domain.board.dto.request;

import com.WalkiePaw.domain.board.entity.BoardStatus;
import lombok.Data;

@Data
public class BoardStatusUpdateRequest {
    private final Long boardId;
    private final BoardStatus status;
}
