package com.WalkiePaw.domain.chatroom.repository;

import com.WalkiePaw.domain.chatroom.entity.Chatroom;
import com.WalkiePaw.domain.member.entity.QMember;
import com.WalkiePaw.domain.review.entity.QReview;
import com.WalkiePaw.global.util.Querydsl4RepositorySupport;
import com.WalkiePaw.presentation.domain.chatroom.response.ChatroomListResponse;
import com.WalkiePaw.presentation.domain.chatroom.response.TransactionResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.WalkiePaw.domain.board.entity.QBoard.board;
import static com.WalkiePaw.domain.chatroom.entity.ChatroomStatus.COMPLETED;
import static com.WalkiePaw.domain.chatroom.entity.QChatroom.*;
import static com.WalkiePaw.domain.member.entity.QMember.member;
import static com.WalkiePaw.domain.review.entity.QReview.review;

@Repository
public class ChatroomRepositoryOverrideImpl extends Querydsl4RepositorySupport implements ChatroomRepositoryOverride {

    public ChatroomRepositoryOverrideImpl() {
        super(Chatroom.class);
    }

    @Override
    public Slice<ChatroomListResponse> findByMemberId(final Long memberId, Pageable pageable) {
        return slice(pageable,
                query -> query.select(
                                Projections.constructor(ChatroomListResponse.class,
                                        chatroom.id,
                                        board.location,
                                        member.nickname,
//                                        Expressions.stringTemplate("CASE WHEN {0} = {1} THEN {2} ELSE {3} END",
//                                                memberId, chatroom.senderId,
//                                                chatroom.board.member.nickname, chatroom.member.nickname).as("nickname"),
                                        chatroom.latestMessage,
                                        chatroom.modifiedDate,
                                        chatroom.unreadCount,
                                        board.title.as("boardTitle"),
                                        member.photo,
//                                        Expressions.stringTemplate("CASE WHEN {0} = {1} THEN {2} ELSE {3} END",
//                                                memberId, chatroom.member.id,
//                                                chatroom.board.member.photo, chatroom.member.photo).as("memberPhoto"),
                                        board.status,
                                        Expressions.asBoolean(chatroom.status.eq(COMPLETED)),
                                        Expressions.asBoolean(chatroom.recipientId.eq(memberId)),
                                        board.category,
                                        member.id
//                                        new CaseBuilder()
//                                                .when(Expressions.asNumber(memberId).eq(chatroom.member.id))
//                                                .then(chatroom.board.member.id)
//                                                .otherwise(chatroom.member.id)
//                                                .as("memberId")
                                ))
                        .from(chatroom)
                        .where(chatroom.senderId.eq(memberId).or(chatroom.recipientId.eq(memberId))));
    }

    @Override
    public Page<TransactionResponse> findTransaction(final Long memberId, Pageable pageable) {
        QMember sender = new QMember("sender");
        QMember recipient = new QMember("recipient");
        return page(pageable,
                page -> page.select(Projections.bean(TransactionResponse.class,
                                chatroom.id.as("chatroomId"),
                                board.title.as("title"),
                                new CaseBuilder()
                                        .when(sender.isNull()).then(recipient.nickname)
                                        .when(recipient.isNull()).then(sender.nickname)
                                        .otherwise(""),
                                chatroom.completedDate.as("createdDate"),
                                review.isNotNull().as("hasReview"),
                                board.category.as("category")
                        ))
                        .from(chatroom)
                        .leftJoin(board).on(board.id.eq(chatroom.boardId))
                        .leftJoin(sender).on(chatroom.senderId.eq(memberId))
                        .leftJoin(recipient).on(chatroom.recipientId.eq(memberId))
                        .leftJoin(review).on(review.chatroomId.eq(chatroom.id)
                                .and(review.reviewerId.eq(memberId)))
                        .where(chatroom.status.eq(COMPLETED))
        );
    }


    @Override
    public Optional<Chatroom> findBySenderIdAndBoardId(final Long memberId, final Long boardId) {
        return Optional.ofNullable(selectFrom(chatroom)
                .where(chatroom.boardId.eq(boardId).and(chatroom.senderId.eq(memberId)))
                .fetchFirst());
    }

    @Override
    public Optional<Chatroom> findByRecipientIdAndBoardId(final Long writerId, final Long boardId) {
        return Optional.ofNullable(selectFrom(chatroom)
                .where(chatroom.boardId.eq(boardId).and(chatroom.recipientId.eq(writerId)))
                .fetchFirst());
    }

}
