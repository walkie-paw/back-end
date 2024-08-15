package com.WalkiePaw.presentation.domain.report.memberReportDto.response;

import com.WalkiePaw.domain.member.entity.Member;
import com.WalkiePaw.domain.report.entity.MemberReport;
import com.WalkiePaw.domain.report.entity.MemberReportCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MemberReportGetResponse {

    private Long memberReportId;
    private String content;
    private MemberReportCategory reason;
    private String reportingMemberName;
    private String reportingMemberNickname;
    private String reportedMemberName;
    private String reportedMemberNickname;

    /**
     * Entity를 DTO로 변환하는 메서드
     */
    public static MemberReportGetResponse from(MemberReport memberReport, Member reportingMember, Member reportedMember) {
        return new MemberReportGetResponse(
                memberReport.getId(),
                memberReport.getContent(),
                memberReport.getReason(),
                reportingMember.getName(), // reportingMemberName
                reportingMember.getNickname(), // reportingMemberNickname
                reportedMember.getName(), // reportedMemberName
                reportedMember.getNickname() // reportedMemberNickname
        );
    }
}
