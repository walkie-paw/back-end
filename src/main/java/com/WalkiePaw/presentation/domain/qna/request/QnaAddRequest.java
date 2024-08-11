package com.WalkiePaw.presentation.domain.qna.request;

import com.WalkiePaw.domain.board.entity.Board;
import com.WalkiePaw.domain.member.entity.Member;
import com.WalkiePaw.domain.qna.entity.Qna;
import com.WalkiePaw.domain.qna.entity.QnaStatus;
import lombok.Getter;

@Getter
public class QnaAddRequest {
    private Long memberId;
    private String title;
    private String content;

    public static Qna toEntity(QnaAddRequest request) {
        return Qna.builder()
                .memberId(request.getMemberId())
                .title(request.getTitle())
                .content(request.getContent())
                .build();
    }
}
