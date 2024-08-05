package com.WalkiePaw.presentation.domain.report.boardReportDto.request;

import com.WalkiePaw.domain.report.entity.BoardReportCategory;
import lombok.Getter;

@Getter
public class BoardReportUpdateRequest {
    private BoardReportCategory reason;
    private String content;
    private Long memberId;
    private Long boardId;
}
