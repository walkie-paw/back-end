package com.WalkiePaw.presentation.domain.report.boardReportDto.request;

import com.WalkiePaw.domain.report.entity.BoardReportCategory;

public record BoardReportUpdateRequest(BoardReportCategory reason, String content, Long memberId, Long boardId) {
}
