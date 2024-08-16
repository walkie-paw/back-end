package com.WalkiePaw.presentation.domain.chat.dto.request;

import com.WalkiePaw.domain.chat.entity.ChatMessage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record ChatAddRequest(
        @Positive Long writerId,
        @NotBlank String content,
        String sentTime,
        @NotBlank String nickname,
        LocalDateTime createdDate) {
    public ChatMessage toEntity(final ChatAddRequest request, final Long chatroomId) {
        return new ChatMessage(chatroomId, request.writerId, request.content);
    }
}
