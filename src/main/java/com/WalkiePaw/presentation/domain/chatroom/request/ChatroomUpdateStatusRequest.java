package com.WalkiePaw.presentation.domain.chatroom.request;

import com.WalkiePaw.domain.board.entity.BoardStatus;
import com.WalkiePaw.domain.chatroom.entity.ChatroomStatus;
import lombok.Data;

@Data
public class ChatroomUpdateStatusRequest {
    private final Long chatroomId;
    private final BoardStatus status;
}
