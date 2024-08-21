package com.WalkiePaw.domain.chatroom.entity;

import com.WalkiePaw.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = {
            @Index(name = "ix_chatroom_recipient_id", columnList = "recipient_id"),
            @Index(name = "ix_chatroom_sender_id", columnList = "sender_id")
        })
public class Chatroom extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatroom_id")
    private Long id;

    private Long boardId;

    private Long recipientId;

    private Long senderId;

    @Enumerated(EnumType.STRING)
    private ChatroomStatus status;
    @Enumerated(EnumType.STRING)
    private ChatroomCategory category;
    private int unreadCount;
    private String latestMessage;
    private LocalDateTime latestMessageTime;
    private LocalDateTime completedDate;

    public Chatroom(Long boardId, Long senderId, Long recipientId) {
        this.boardId = boardId;
        this.recipientId = recipientId;
        this.senderId = senderId;
        this.status = ChatroomStatus.RECRUITING;
    }

    public void updateLatestMessage(final String latestMessage) {
        this.latestMessage = latestMessage;
    }

    public void updateStatus(final ChatroomStatus chatroomStatus) {
        this.status = chatroomStatus;
        this.completedDate = LocalDateTime.now();
    }

//    /**
//     * Chatroom 생성 메서드
//     * @param board 어떤 게시물과 연결된 채팅인지, board의 작성자가 채팅을 받는 member가 된다.
//     * @param member 채팅을 시작한 member
//     */
//    public Chatroom createChatroom(Board board, Member member) {
//        return new Chatroom(board, member);
//    }
}
