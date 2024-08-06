package com.WalkiePaw.presentation.domain.report.memberReportDto.response;

import com.WalkiePaw.domain.member.entity.Member;
import com.WalkiePaw.domain.report.entity.MemberReport;
import com.WalkiePaw.domain.report.entity.MemberReportCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class MemberReportListResponse {

    private Long memberReportId;
    private String title;
    private MemberReportCategory reason;
    private String reportingMemberName;
    private String reportingMemberNickname;
    private String reportedMemberName;
    private String reportedMemberNickname;
    private LocalDateTime createdDate;

    /**
     * Entity를 DTO로 변환하는 메서드
     */
    public static MemberReportListResponse from(MemberReport memberReport, Member reportingMember, Member reportedMember) {
        return new MemberReportListResponse(
                memberReport.getId(),
                memberReport.getTitle(),
                memberReport.getReason(),
                reportingMember.getName(), // reportingMemberName
                reportingMember.getNickname(), // reportingMemberNickname
                reportedMember.getName(), // reportedMemberName
                reportedMember.getNickname(), // reportedMemberNickname
                memberReport.getCreatedDate()
        );
    }
}
