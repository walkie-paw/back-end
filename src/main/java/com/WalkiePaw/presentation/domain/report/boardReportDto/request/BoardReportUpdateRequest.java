package com.WalkiePaw.presentation.domain.report.boardReportDto.request;

import com.WalkiePaw.domain.report.entity.BoardReportCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record BoardReportUpdateRequest(
        @NotNull BoardReportCategory reason,
        @NotBlank String content,
        @Positive Long memberId,
        @Positive Long boardId) {
}
