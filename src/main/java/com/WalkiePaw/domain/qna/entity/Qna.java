package com.WalkiePaw.domain.qna.entity;

import com.WalkiePaw.domain.common.BaseEntity;
import com.WalkiePaw.presentation.domain.qna.request.QnaUpdateRequest;
import com.WalkiePaw.presentation.domain.qna.request.ReplyUpdateRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
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

    public void update(QnaUpdateRequest request) {
        this.title = request.getTitle();
        this.content = request.getContent();
        this.reply = request.getReply();
        this.status = request.getStatus();
    }

    public void updateReply(final ReplyUpdateRequest request) {
        this.reply = request.getReply();
        this.status = QnaStatus.COMPLETED;
    }

//    /**
//     * QnA 생성 메서드
//     */
//    public Qna createQnA(Member member, String title, String content, LocalDate createdDate) {
//        return new Qna(member, title, content, createdDate);
//    }
}
