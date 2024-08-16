package com.WalkiePaw.presentation.domain.chat.dto;

import com.WalkiePaw.domain.chat.entity.ChatMessage;
import com.WalkiePaw.presentation.domain.chat.dto.request.ChatAddRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChatAddParam {
    @Positive
    private final Long writerId;
    @NotBlank
    private final String content;
    @NotBlank
    private final String nickname;
    private final String sentTime;
    private final LocalDateTime createdDate;

    public ChatAddParam(ChatAddRequest request) {
        this.writerId = request.writerId();
        this.content = request.content();
        this.sentTime = request.sentTime();
        this.nickname = request.nickname();
        this.createdDate = request.createdDate();
    }

    public ChatMessage toEntity(final ChatAddParam request, final Long chatroomId) {
        return new ChatMessage(chatroomId, request.writerId, request.content);
    }
}
