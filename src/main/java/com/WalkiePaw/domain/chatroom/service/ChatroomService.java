package com.WalkiePaw.domain.chatroom.service;

import com.WalkiePaw.domain.board.entity.Board;
import com.WalkiePaw.domain.board.entity.BoardStatus;
import com.WalkiePaw.domain.board.repository.BoardRepository;
import com.WalkiePaw.domain.chatroom.entity.Chatroom;
import com.WalkiePaw.domain.chatroom.entity.ChatroomStatus;
import com.WalkiePaw.domain.chatroom.repository.ChatroomRepository;
import com.WalkiePaw.global.exception.BadRequestException;
import com.WalkiePaw.domain.member.Repository.MemberRepository;
import com.WalkiePaw.presentation.domain.chatroom.request.ChatroomAddRequest;
import com.WalkiePaw.presentation.domain.chatroom.response.ChatroomListResponse;
import com.WalkiePaw.presentation.domain.chatroom.response.ChatroomRespnose;
import com.WalkiePaw.presentation.domain.chatroom.response.TransactionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.WalkiePaw.global.exception.ExceptionCode.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ChatroomService {
    private final ChatroomRepository chatroomRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    public Slice<ChatroomListResponse> findAllByMemberId(final Long memberId, Pageable pageable) {
        return chatroomRepository.findByMemberId(memberId, pageable);
    }

    @Transactional
    public Long saveChatroom(final ChatroomAddRequest request) {
        existsBoard(request);
        existsMember(request);
        Chatroom chatroom = ChatroomAddRequest.toEntity(request.getBoardId(), request.getSenderId(), request.getRecipientId());
        return chatroomRepository.save(chatroom).getId();
    }

    private void existsMember(ChatroomAddRequest request) {
        boolean existsRecipient = memberRepository.existsById(request.getRecipientId());
        boolean existsSender = memberRepository.existsById(request.getSenderId());
        if (!existsSender || !existsRecipient) {
            throw new BadRequestException(NOT_FOUND_MEMBER_ID);
        }
    }

    private void existsBoard(ChatroomAddRequest request) {
        boolean existsBoard = boardRepository.existsById(request.getBoardId());
        if (!existsBoard) {
            throw new BadRequestException(NOT_FOUND_BOARD_ID);
        }
    }

    /**
     * TODO - 로직 수정하기
     */
    public ChatroomRespnose findChatroomById(final Long memberId, final Long boardId) {
        Chatroom chatroom = chatroomRepository.findByMemberIdAndBoardId(memberId, boardId)
                .orElseGet(() ->
                        chatroomRepository.findByWriterIdAndBoardId(memberId, boardId)
                                .orElseThrow(() -> new BadRequestException(NOT_FOUND_CHATROOM_ID)));
        return ChatroomRespnose.toEntity(chatroom);
    }

    public Page<TransactionResponse> findTransaction(final Long memberId, final Pageable pageable) {
        return chatroomRepository.findTransaction(memberId, pageable);
    }

    @Transactional
    public void updateChatroomStatus(
            final Long chatroomId,
            final BoardStatus status
    ) {
        Chatroom chatroom = chatroomRepository.findChatroomAndBoardById(chatroomId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_CHATROOM_ID));
        // BoardStatus 업데이트
        Board board = boardRepository.findById(chatroom.getBoardId())
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_CHATROOM_ID));
        board.updateStatus(status);
        // ChatroomStatus 업데이트
        ChatroomStatus chatroomStatus = ChatroomStatus.valueOf(status.name());
        chatroom.updateStatus(chatroomStatus);
    }
}
