package com.WalkiePaw.presentation.domain.chat.dto.request;

import com.WalkiePaw.domain.chat.entity.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ChatAddRequest {
    private final Long writerId;
    private final String content;
    private final String sentTime;
    private final String nickname;
    private final LocalDateTime createdDate;

    public ChatMessage toEntity(final ChatAddRequest request, final Long chatroomId) {
        return new ChatMessage(chatroomId, request.writerId, request.content);
    }
}
