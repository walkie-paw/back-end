package com.WalkiePaw.domain.report.entity;

import com.WalkiePaw.domain.common.BaseEntity;
import com.WalkiePaw.domain.member.entity.Member;
import com.WalkiePaw.presentation.domain.report.memberReportDto.request.MemberReportUpdateRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberReport extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_report_id")
    private Long id;
    @Column(name = "member_report_title")
    private String title;
    @Column(name = "member_report_content")
    private String content;
    @Enumerated(EnumType.STRING)
    private MemberReportCategory reason;

    private Long reportingMemberId;
    private Long reportedMemberId;
    @Enumerated(EnumType.STRING)
    private MemberReportStatus status;

    @Builder
    public MemberReport(String title, String content, MemberReportCategory reason, Long reportingMemberId, Long reportedMemberId) {
        this.title = title;
        this.content = content;
        this.reason = reason;
        this.reportingMemberId = reportingMemberId;
        this.reportedMemberId = reportedMemberId;
        this.status = MemberReportStatus.UNRESOLVED;
    }

    public void update(String content, MemberReportCategory reason) {
        this.content = content;
        this.reason = reason;
    }

    public void ban() {
        this.status = MemberReportStatus.BANNED;
    }

    public void ignore() {
        this.status = MemberReportStatus.IGNORE;
    }

//    /**
//     * MemberReport 생성 메서드
//     */
//    public MemberReport createMemberReport(String title, String content, LocalDate reportedDate, MemberReportCategory reason, Member reportingMember, Member reportedMember) {
//        return new MemberReport(title, content, reportedDate, reason, reportingMember, reportedMember);
//    }
}
