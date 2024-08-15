package com.WalkiePaw.presentation.domain.report.boardReportDto;

import com.WalkiePaw.domain.report.entity.BoardReportCategory;
import com.WalkiePaw.presentation.domain.report.boardReportDto.request.BoardReportUpdateRequest;
import lombok.Getter;

@Getter
public class BoardReportUpdateParam {

    private final BoardReportCategory reason;
    private final String content;
    private final Long memberId;
    private final Long boardId;

    public BoardReportUpdateParam(BoardReportUpdateRequest request) {
        this.reason = request.reason();
        this.content = request.content();
        this.memberId = request.memberId();
        this.boardId = request.boardId();
    }
}
