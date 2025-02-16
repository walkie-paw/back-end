package com.WalkiePaw.domain.chatroom.repository;

import com.WalkiePaw.domain.chatroom.entity.Chatroom;
import com.WalkiePaw.presentation.domain.chatroom.dto.response.ChatroomListResponse;
import com.WalkiePaw.presentation.domain.chatroom.dto.response.TransactionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.Optional;

public interface ChatroomRepositoryOverride {
    Slice<ChatroomListResponse> findByMemberId(Long memberId, int pageSize, Long cursor);

    Page<TransactionResponse> findTransaction(Long memberId, Pageable pageable);

    Optional<Chatroom> findBySenderIdAndBoardId(Long MemberId, Long boardId);

    Optional<Chatroom> findByRecipientIdAndBoardId(Long writerId, Long boardId);
}
