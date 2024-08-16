package com.WalkiePaw.presentation.domain.chatroom.dto.request;

import com.WalkiePaw.domain.board.entity.BoardStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ChatroomStatusUpdateRequest(
        @Positive Long chatroomId,
        @NotNull BoardStatus status) {
}
