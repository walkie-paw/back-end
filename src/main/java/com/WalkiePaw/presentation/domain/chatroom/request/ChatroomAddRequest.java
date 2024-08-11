package com.WalkiePaw.presentation.domain.chatroom.request;

import com.WalkiePaw.domain.board.entity.Board;
import com.WalkiePaw.domain.chatroom.entity.Chatroom;
import com.WalkiePaw.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatroomAddRequest {

    private final Long boardId;
    private final Long senderId;
    private final Long recipientId;

    public static Chatroom toEntity(
            final Long boardId,
            final Long senderId,
            final Long recipientId
            ) {
        return new Chatroom(boardId, senderId, recipientId);
    }
}
