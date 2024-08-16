package com.WalkiePaw.presentation.domain.chat;

import com.WalkiePaw.domain.chat.service.ChatService;
import com.WalkiePaw.presentation.domain.chat.dto.request.ChatAddRequest;
import com.WalkiePaw.presentation.domain.chat.dto.response.ChatWebSocketResponse;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final ChatService chatService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/chats/{chatroomId}")
    @SendTo("/chats/{chatroomId}")
    public void addChat(@DestinationVariable("chatroomId") @Positive Long chatroomId, @Payload @Validated ChatAddRequest request) {
        String destination = "/chats/" + chatroomId;
        ChatWebSocketResponse response = new ChatWebSocketResponse(request.writerId(), request.nickname(), request.content(), request.sentTime());
        simpMessagingTemplate.convertAndSend(destination, response);
        chatService.saveChatMsg(chatroomId, request);
    }
}
