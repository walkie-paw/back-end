package com.WalkiePaw.presentation.domain.report.memberReportDto;

import com.WalkiePaw.domain.report.entity.MemberReport;
import com.WalkiePaw.domain.report.entity.MemberReportCategory;
import com.WalkiePaw.presentation.domain.report.memberReportDto.request.MemberReportAddRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

@Getter
public class MemberReportAddParam {

    private final @NotBlank String content;
    private final @NotNull MemberReportCategory reason;
    private final @Positive Long reportingMemberId;
    private final @Positive Long reportedMemberId;

    public MemberReportAddParam(MemberReportAddRequest request) {
        this.content = request.content();
        this.reason = request.reason();
        this.reportingMemberId = request.reportingMemberId();
        this.reportedMemberId = request.reportedMemberId();
    }

    public static MemberReport toEntity(MemberReportAddParam param) {
        return MemberReport.builder()
                .content(param.content)
                .reason(param.reason)
                .reportingMemberId(param.getReportingMemberId())
                .reportedMemberId(param.getReportedMemberId())
                .build();
    }
}
