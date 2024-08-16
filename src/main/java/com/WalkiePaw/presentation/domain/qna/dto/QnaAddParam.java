package com.WalkiePaw.presentation.domain.qna.dto;

import com.WalkiePaw.domain.qna.entity.Qna;
import com.WalkiePaw.presentation.domain.qna.dto.request.QnaAddRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

@Getter
public class QnaAddParam {

    private final @Positive Long memberId;
    private final @NotBlank String title;
    private final @NotBlank String content;

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
