package com.WalkiePaw.presentation.domain.report.memberReportDto.request;

import com.WalkiePaw.domain.member.entity.Member;
import com.WalkiePaw.domain.report.entity.MemberReport;
import com.WalkiePaw.domain.report.entity.MemberReportCategory;
import lombok.Getter;

@Getter
public class MemberReportAddRequest {
    private String content;
    private MemberReportCategory reason;
    private Long reportingMemberId;
    private Long reportedMemberId;

    /**
     * DTO를 Entity로 변환하는 메서드
     */
    public static MemberReport toEntity(MemberReportAddRequest request, Long reportingMemberId, Long reportedMemberId) {
        return MemberReport.builder()
                .content(request.content)
                .reason(request.reason)
                .reportingMemberId(reportingMemberId)
                .reportedMemberId(reportedMemberId)
                .build();

    }
}
