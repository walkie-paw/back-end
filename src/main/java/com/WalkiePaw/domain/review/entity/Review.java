package com.WalkiePaw.domain.review.entity;

import com.WalkiePaw.domain.board.entity.BoardCategory;
import com.WalkiePaw.domain.common.BaseEntity;
import com.WalkiePaw.domain.chatroom.entity.Chatroom;
import com.WalkiePaw.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = {
        @Index(name = "ix_review_reviewee_id", columnList = "reviewee_id"),
        @Index(name = "ix_review_reviewer_id", columnList = "reviewer_id")
})
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;
    private Long revieweeId;
    private Long reviewerId;
    private Long chatroomId;
    @Column(name = "review_content")
    private String content;
    @Enumerated(EnumType.STRING)
    private BoardCategory category;
    private int point;
    private boolean isDeleted;

    @Builder
    public Review(int point, String content, Long chatroomId, Long revieweeId, Long reviewerId, BoardCategory category) {
        this.category = category;
        this.point = point;
        this.content = content;
        this.chatroomId = chatroomId;
        this.revieweeId = revieweeId;
        this.reviewerId = reviewerId;
    }

    /**
     * TODO - update 메서드
     */
    public void update(final String content, final int point) {
        this.content = content;
        this.point = point;
    }

//    public Review createReview(int point, String content, Chatroom chatroom, Member member) {
//        return new Review(point, content, chatroom, member);
//    }
}
