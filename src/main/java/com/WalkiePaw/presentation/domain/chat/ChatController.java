package com.WalkiePaw.presentation.domain.chat;

import com.WalkiePaw.domain.chat.service.ChatService;
import com.WalkiePaw.presentation.domain.chat.dto.response.ChatMsgListResponse;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/chats")
public class ChatController {
    public static final String CHAT_URI = "/api/v1/chats/";
    private final ChatService chatService;
    private final SimpMessagingTemplate webSocket;

    @GetMapping
    @ResponseStatus(OK)
    public List<ChatMsgListResponse> getChatListByChatroomId(final @RequestParam @Positive Long chatroomId) {
        return chatService.findChatsByChatroomId(chatroomId);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(NO_CONTENT )
    public void chatMsgUpdateIsRead(final @PathVariable("id") @Positive Long chatroomId) {
        chatService.bulkUpdateIsRead(chatroomId);
    }
}
