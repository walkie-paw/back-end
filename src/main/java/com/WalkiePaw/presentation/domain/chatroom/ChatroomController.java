package com.WalkiePaw.presentation.domain.chatroom;

import com.WalkiePaw.domain.chatroom.service.ChatroomService;
import com.WalkiePaw.presentation.domain.chatroom.dto.ChatroomAddParam;
import com.WalkiePaw.presentation.domain.chatroom.dto.request.ChatroomAddRequest;
import com.WalkiePaw.presentation.domain.chatroom.dto.request.ChatroomStatusUpdateRequest;
import com.WalkiePaw.presentation.domain.chatroom.dto.response.ChatroomListResponse;
import com.WalkiePaw.presentation.domain.chatroom.dto.response.ChatroomRespnose;
import com.WalkiePaw.presentation.domain.chatroom.dto.response.TransactionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/chatrooms")
public class ChatroomController {
    private final ChatroomService chatroomService;
    private static final String CHATROOM_URI = "/chatrooms/";

    @GetMapping
    public ResponseEntity<Slice<ChatroomListResponse>> getChatroomList(@RequestParam("id") final Long memberId, Pageable pageable) {
        var chatrooms = chatroomService.findAllByMemberId(memberId, pageable);
        return ResponseEntity.ok(chatrooms);
    }

    @PostMapping
    public ResponseEntity<Void> addChatroom(final @RequestBody ChatroomAddRequest request) {
        var param = new ChatroomAddParam(request);
        Long id = chatroomService.saveChatroom(param);
        return ResponseEntity.created(URI.create(CHATROOM_URI + id)).build();
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<ChatroomRespnose> getChatroom(final @PathVariable Long memberId, final @RequestParam("board_id") Long boardId) {
        ChatroomRespnose chatroomById = chatroomService.findChatroomById(memberId, boardId);
        return ResponseEntity.ok(chatroomById);
    }

    @GetMapping("/{id}/transaction")
    public ResponseEntity<Page<TransactionResponse>> getTransaction(final @PathVariable("id") Long memberId, Pageable pageable) {
        var transaction = chatroomService.findTransaction(memberId, pageable);
        return ResponseEntity.ok(transaction);
    }

    @PatchMapping("/change-status")
    public ResponseEntity<Void> updateChatroomStatus(
            final @RequestBody ChatroomStatusUpdateRequest request
            ) {
        chatroomService.updateChatroomStatus(request.chatroomId(), request.status());
        return ResponseEntity.noContent().build();
    }


}
