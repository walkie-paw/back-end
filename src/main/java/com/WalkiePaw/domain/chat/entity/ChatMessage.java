package com.WalkiePaw.domain.chat.entity;

import com.WalkiePaw.domain.common.BaseEntity;
import com.WalkiePaw.domain.chatroom.entity.Chatroom;
import com.WalkiePaw.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = @Index(name = "ix_chat_message_chatroom_id_writer_id", columnList = "chatroom_id, writer_id"))
public class ChatMessage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_msg_id")
    private Long id;

    private Long chatroomId;

    private Long writerId;

    private boolean isRead;

    @Column(name = "msg_content")
    private String content;

    public ChatMessage(Long chatroomId, Long writerId, String content) {
        this.chatroomId = chatroomId;
        this.writerId = writerId;
        this.content = content;
    }

    public ChatMessage(final Long writerId, final String content) {
        this.writerId = writerId;
        this.content = content;
    }
    //    /**
//     * ChatMessage 생성 메서드
//     * @param receiver 받을 member를 가르킴
//     */
//    public ChatMessage createChatMessage(Chatroom chatroom, Member receiver, String content) {
//        return new ChatMessage(chatroom, receiver, content);
//    }
}
