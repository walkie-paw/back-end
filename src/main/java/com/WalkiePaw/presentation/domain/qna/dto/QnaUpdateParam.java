package com.WalkiePaw.presentation.domain.qna.dto;

import com.WalkiePaw.domain.qna.entity.QnaStatus;
import com.WalkiePaw.presentation.domain.qna.dto.request.QnaUpdateRequest;
import lombok.Getter;

@Getter
public class QnaUpdateParam {

    private final Long memberId;
    private final String title;
    private final String content;
    private final String reply;
    private final QnaStatus status;

    public QnaUpdateParam(QnaUpdateRequest request) {
        this.memberId = request.memberId();
        this.title = request.title();
        this.content = request.content();
        this.reply = request.reply();
        this.status = request.status();
    }
}
