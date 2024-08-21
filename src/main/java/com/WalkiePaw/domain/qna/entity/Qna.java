package com.WalkiePaw.domain.qna.entity;

import com.WalkiePaw.domain.common.BaseEntity;
import com.WalkiePaw.presentation.domain.qna.dto.request.QnaUpdateRequest;
import com.WalkiePaw.presentation.domain.qna.dto.request.ReplyUpdateRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(indexes = @Index(name = "ix_qna_member_id", columnList = "member_id"))
public class Qna extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qna_id")
    private Long id;
    private Long memberId;
    private String title;
    private String content;
    private String reply;
    @Enumerated(EnumType.STRING)
    private QnaStatus status;

    @Builder
    public Qna(Long memberId, String title, String content) {
        this.memberId = memberId;
        this.title = title;
        this.content = content;
        this.status = QnaStatus.WAITING;
    }

    public void update(final String title, final String content, final String reply, final QnaStatus status) {
        this.title = title;
        this.content = content;
        this.reply = reply;
        this.status = status;
    }

    public void updateReply(final String reply) {
        this.reply = reply;
        this.status = QnaStatus.COMPLETED;
    }

//    /**
//     * QnA 생성 메서드
//     */
//    public Qna createQnA(Member member, String title, String content, LocalDate createdDate) {
//        return new Qna(member, title, content, createdDate);
//    }
}
