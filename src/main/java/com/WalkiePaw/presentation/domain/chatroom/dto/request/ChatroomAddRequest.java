package com.WalkiePaw.presentation.domain.chatroom.dto.request;

import lombok.Data;

public record ChatroomAddRequest(Long boardId, Long senderId, Long recipientId) {
}
