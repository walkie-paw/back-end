package com.WalkiePaw.presentation.domain.chatroom;

import com.WalkiePaw.domain.chatroom.service.ChatroomService;
import com.WalkiePaw.presentation.domain.chatroom.dto.ChatroomAddParam;
import com.WalkiePaw.presentation.domain.chatroom.dto.request.ChatroomAddRequest;
import com.WalkiePaw.presentation.domain.chatroom.dto.request.ChatroomStatusUpdateRequest;
import com.WalkiePaw.presentation.domain.chatroom.dto.response.ChatroomListResponse;
import com.WalkiePaw.presentation.domain.chatroom.dto.response.ChatroomRespnose;
import com.WalkiePaw.presentation.domain.chatroom.dto.response.TransactionResponse;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/chatrooms")
public class ChatroomController {
    private final ChatroomService chatroomService;
    private static final String CHATROOM_URI = "/api/v1/chatrooms/";

    @GetMapping
    @ResponseStatus(OK)
    public Slice<ChatroomListResponse> getChatroomList(
            final @RequestParam("id") @Positive Long memberId,
            final @RequestParam("page_size") int pageSize,
            final @RequestParam Long cursor
    ) {
        return chatroomService.findAllByMemberId(memberId, pageSize, cursor);
    }

    @PostMapping
    public ResponseEntity<Void> addChatroom(final @RequestBody @Validated ChatroomAddRequest request) {
        var param = new ChatroomAddParam(request);
        Long id = chatroomService.saveChatroom(param);
        return ResponseEntity.created(URI.create(CHATROOM_URI + id)).build();
    }

    @GetMapping("/{memberId}")
    @ResponseStatus(OK)
    public ChatroomRespnose getChatroom(final @PathVariable @Positive Long memberId, final @RequestParam("board_id") @Positive Long boardId) {
        return chatroomService.findChatroomById(memberId, boardId);
    }

    @GetMapping("/{id}/transaction")
    @ResponseStatus(OK)
    public Page<TransactionResponse> getTransaction(final @PathVariable("id") @Positive Long memberId, Pageable pageable) {
        return chatroomService.findTransaction(memberId, pageable);
    }

    @PatchMapping("/change-status")
    @ResponseStatus(NO_CONTENT)
    public void updateChatroomStatus(final @RequestBody @Validated ChatroomStatusUpdateRequest request) {
        chatroomService.updateChatroomStatus(request.chatroomId(), request.status());
    }
}
