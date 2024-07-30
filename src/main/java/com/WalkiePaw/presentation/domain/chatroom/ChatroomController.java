package com.WalkiePaw.presentation.domain.chatroom;

import com.WalkiePaw.presentation.domain.chatroom.request.ChatroomUpdateStatusRequest;
import com.WalkiePaw.presentation.domain.chatroom.response.TransactionResponse;
import com.WalkiePaw.domain.chatroom.service.ChatroomService;
import com.WalkiePaw.presentation.domain.chatroom.request.ChatroomAddRequest;
import com.WalkiePaw.presentation.domain.chatroom.response.ChatroomListResponse;
import com.WalkiePaw.presentation.domain.chatroom.response.ChatroomRespnose;
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

///api/v1/{domain}-> get : 목록
///api/v1/{domain} -> post : 등록
///api/v1/{domain}/{id} -> get : 조회
///api/v1/{domain}/{id} -> patch  : 수정
///api/v1/{domain}/{id} -> delete : 삭제

    @GetMapping
    public ResponseEntity<Slice<ChatroomListResponse>> getChatroomList(@RequestParam("id") final Long memberId, Pageable pageable) {
        Slice<ChatroomListResponse> chatrooms = chatroomService.findAllByMemberId(memberId, pageable);
        return ResponseEntity.ok(chatrooms);
    }

    @PostMapping
    public ResponseEntity<Void> addChatroom(final @RequestBody ChatroomAddRequest request) {
        Long id = chatroomService.saveChatroom(request);
        return ResponseEntity.created(URI.create(CHATROOM_URI + id)).build();
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<ChatroomRespnose> getChatroom(final @PathVariable Long memberId, final @RequestParam("board_id") Long boardId) {
        ChatroomRespnose chatroomById = chatroomService.findChatroomById(memberId, boardId);
        return ResponseEntity.ok(chatroomById);
    }

    @GetMapping("/{id}/transaction")
    public ResponseEntity<Page<TransactionResponse>> getTransaction(final @PathVariable("id") Long memberId, Pageable pageable) {
        Page<TransactionResponse> transaction = chatroomService.findTransaction(memberId, pageable);
        return ResponseEntity.ok(transaction);
    }

    @PatchMapping("/change-status")
    public ResponseEntity<Void> updateChatroomStatus(
            final @RequestBody ChatroomUpdateStatusRequest request
            ) {
        chatroomService.updateChatroomStatus(request.getChatroomId(), request.getStatus());
        return ResponseEntity.noContent().build();
    }


}
