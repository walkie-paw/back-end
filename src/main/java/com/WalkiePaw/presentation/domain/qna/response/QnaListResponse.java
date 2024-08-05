package com.WalkiePaw.presentation.domain.qna.response;

import com.WalkiePaw.domain.member.entity.Member;
import com.WalkiePaw.domain.qna.entity.Qna;
import com.WalkiePaw.domain.qna.entity.QnaStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class QnaListResponse {

    private Long qnaId;
    private Long memberId;
    private String writerName;
    private String title;
    private QnaStatus status;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    /**
     * 엔티티를 dto로 변환하는 메서드
     */
    public static QnaListResponse from(Qna qna, Member member) {
        return new QnaListResponse(
                qna.getId(),
                member.getId(),
                member.getName(),
                qna.getTitle(),
                qna.getStatus(),
                qna.getCreatedDate(),
                qna.getModifiedDate()
        );
    }
}
