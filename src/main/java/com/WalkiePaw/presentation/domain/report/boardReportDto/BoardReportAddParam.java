package com.WalkiePaw.presentation.domain.report.boardReportDto;

import com.WalkiePaw.domain.report.entity.BoardReport;
import com.WalkiePaw.domain.report.entity.BoardReportCategory;
import com.WalkiePaw.presentation.domain.report.boardReportDto.request.BoardReportAddRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

@Getter
public class BoardReportAddParam {

    private final @NotNull BoardReportCategory reason;
    private final @NotBlank String content;
    private final @Positive Long memberId;
    private final @Positive Long boardId;

    public BoardReportAddParam(BoardReportAddRequest request) {
        this.reason = request.reason();
        this.content = request.content();
        this.memberId = request.memberId();
        this.boardId = request.boardId();
    }

    public static BoardReport toEntity(BoardReportAddParam param) {
        return BoardReport.builder()
                .content(param.content)
                .reason(param.reason)
                .memberId(param.memberId)
                .boardId(param.boardId)
                .build();
    }

}
