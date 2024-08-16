package com.WalkiePaw.presentation.domain.report.memberReportDto.request;

import com.WalkiePaw.domain.report.entity.MemberReportCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record MemberReportUpdateRequest(
        @NotBlank String content,
        @NotNull MemberReportCategory reason,
        @Positive Long reportingMemberId,
        @Positive Long reportedMemberId) {
}
