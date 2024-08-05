package com.WalkiePaw.presentation.domain.chat.response;

import com.WalkiePaw.domain.chat.entity.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ChatMsgListResponse {

    private final Long writerId;
    private final String nickname;
    private final String content;
    private final LocalDateTime createDate;

    public static ChatMsgListResponse from(ChatMessage chatMessage, String nickname) {
        return new ChatMsgListResponse(chatMessage.getWriterId(), nickname, chatMessage.getContent(), chatMessage.getCreatedDate());
    }
}
