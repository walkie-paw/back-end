package com.WalkiePaw.domain.chatroom.service;

import com.WalkiePaw.domain.board.entity.Board;
import com.WalkiePaw.domain.board.entity.BoardStatus;
import com.WalkiePaw.domain.board.repository.BoardRepository;
import com.WalkiePaw.domain.chatroom.entity.Chatroom;
import com.WalkiePaw.domain.chatroom.entity.ChatroomStatus;
import com.WalkiePaw.domain.chatroom.repository.ChatroomRepository;
import com.WalkiePaw.domain.member.Repository.MemberRepository;
import com.WalkiePaw.global.exception.BadRequestException;
import com.WalkiePaw.presentation.domain.chatroom.dto.ChatroomAddParam;
import com.WalkiePaw.presentation.domain.chatroom.dto.response.ChatroomListResponse;
import com.WalkiePaw.presentation.domain.chatroom.dto.response.ChatroomRespnose;
import com.WalkiePaw.presentation.domain.chatroom.dto.response.TransactionResponse;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import static com.WalkiePaw.global.exception.ExceptionCode.*;

@Service
@RequiredArgsConstructor
@Validated
@Transactional(readOnly = true)
public class ChatroomService {
    private final ChatroomRepository chatroomRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    public Slice<ChatroomListResponse> findAllByMemberId(final @Positive Long memberId, Pageable pageable) {
        return chatroomRepository.findByMemberId(memberId, pageable);
    }

    @Transactional
    public Long saveChatroom(final @Validated ChatroomAddParam param) {
        existsBoard(param);
        existsMember(param);
        Chatroom chatroom = ChatroomAddParam.toEntity(param.getBoardId(), param.getSenderId(), param.getRecipientId());
        return chatroomRepository.save(chatroom).getId();
    }

    private void existsMember(final @Validated ChatroomAddParam param) {
        boolean existsRecipient = memberRepository.existsById(param.getRecipientId());
        boolean existsSender = memberRepository.existsById(param.getSenderId());
        if (!existsSender || !existsRecipient) {
            throw new BadRequestException(NOT_FOUND_MEMBER_ID);
        }
    }

    private void existsBoard(final @Validated ChatroomAddParam param) {
        boolean existsBoard = boardRepository.existsById(param.getBoardId());
        if (!existsBoard) {
            throw new BadRequestException(NOT_FOUND_BOARD_ID);
        }
    }

    /**
     * TODO - 로직 수정하기
     */
    public ChatroomRespnose findChatroomById(final @Positive Long memberId, final @Positive Long boardId) {
        Chatroom chatroom = chatroomRepository.findBySenderIdAndBoardId(memberId, boardId)
                .orElseGet(() ->
                        chatroomRepository.findByRecipientIdAndBoardId(memberId, boardId)
                                .orElseThrow(() -> new BadRequestException(NOT_FOUND_CHATROOM_ID)));
        return ChatroomRespnose.toEntity(chatroom);
    }

    public Page<TransactionResponse> findTransaction(final @Positive Long memberId, final Pageable pageable) {
        return chatroomRepository.findTransaction(memberId, pageable);
    }

    @Transactional
    public void updateChatroomStatus(
            final @Positive Long chatroomId,
            final @NotNull BoardStatus status
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
