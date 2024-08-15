package com.WalkiePaw.presentation.domain.chat.dto.response;

import lombok.Data;

@Data
public class ChatWebSocketResponse {
    private final Long writerId;
    private final String nickname;
    private final String content;
    private final String sentTime;
}
