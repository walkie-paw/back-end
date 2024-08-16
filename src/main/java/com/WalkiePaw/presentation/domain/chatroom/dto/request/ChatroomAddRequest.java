package com.WalkiePaw.presentation.domain.chatroom.dto.request;

import jakarta.validation.constraints.Positive;

public record ChatroomAddRequest(
        @Positive Long boardId,
        @Positive Long senderId,
        @Positive Long recipientId) {
}
