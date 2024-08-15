package com.WalkiePaw.presentation.domain.chatroom.dto.request;

import com.WalkiePaw.domain.board.entity.BoardStatus;

public record ChatroomStatusUpdateRequest(Long chatroomId, BoardStatus status) {
}
