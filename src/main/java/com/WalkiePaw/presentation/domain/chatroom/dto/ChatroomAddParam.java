package com.WalkiePaw.presentation.domain.chatroom.dto;

import com.WalkiePaw.domain.chatroom.entity.Chatroom;
import com.WalkiePaw.presentation.domain.chatroom.dto.request.ChatroomAddRequest;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

@Getter
public class ChatroomAddParam {

    private final @Positive Long boardId;
    private final @Positive Long senderId;
    private final @Positive Long recipientId;

    public ChatroomAddParam(ChatroomAddRequest request) {
        this.boardId = request.boardId();
        this.senderId = request.senderId();
        this.recipientId = request.recipientId();
    }

    public static Chatroom toEntity(
            final Long boardId,
            final Long senderId,
            final Long recipientId
    ) {
        return new Chatroom(boardId, senderId, recipientId);
    }
}
