package com.WalkiePaw.presentation.domain.qna.dto.response;

import com.WalkiePaw.domain.member.entity.Member;
import com.WalkiePaw.domain.qna.entity.Qna;
import com.WalkiePaw.domain.qna.entity.QnaStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class QnaGetResponse {

    private Long qnaId;
    private String writerName;
    private String title;
    private String content;
    private String reply;
    private QnaStatus status;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    /**
     * 엔티티를 dto로 변환하는 메서드
     */
    public static QnaGetResponse from(Qna qna, Member member) {
        return new QnaGetResponse(
                qna.getId(),
                member.getName(),
                qna.getTitle(),
                qna.getContent(),
                qna.getReply(),
                qna.getStatus(),
                qna.getCreatedDate(),
                qna.getModifiedDate()
        );
    }
}
