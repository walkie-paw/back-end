package com.WalkiePaw.domain.transaction.entity;

import com.WalkiePaw.domain.chatroom.entity.Chatroom;
import com.WalkiePaw.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Transaction extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long id;
    private Long chatroomId;
    private Long senderId;
    private Long recipientId;
    private double rating;
}
