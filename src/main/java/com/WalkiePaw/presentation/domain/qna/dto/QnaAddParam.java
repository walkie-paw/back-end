package com.WalkiePaw.presentation.domain.qna.dto;

import com.WalkiePaw.domain.qna.entity.Qna;
import com.WalkiePaw.presentation.domain.qna.dto.request.QnaAddRequest;
import lombok.Getter;

@Getter
public class QnaAddParam {

    private final Long memberId;
    private final String title;
    private final String content;

    public QnaAddParam(QnaAddRequest request) {
        this.memberId = request.memberId();
        this.title = request.title();
        this.content = request.content();
    }

    public static Qna toEntity(QnaAddParam param) {
        return Qna.builder()
                .memberId(param.getMemberId())
                .title(param.getTitle())
                .content(param.getContent())
                .build();
    }
}
