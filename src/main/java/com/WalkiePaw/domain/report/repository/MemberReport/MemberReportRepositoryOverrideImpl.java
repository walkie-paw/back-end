package com.WalkiePaw.domain.report.repository.MemberReport;

import com.WalkiePaw.domain.member.entity.QMember;
import com.WalkiePaw.domain.report.entity.MemberReport;
import com.WalkiePaw.domain.report.entity.MemberReportStatus;
import com.WalkiePaw.global.util.Querydsl4RepositorySupport;
import com.WalkiePaw.presentation.domain.report.memberReportDto.response.MemberReportGetResponse;
import com.WalkiePaw.presentation.domain.report.memberReportDto.response.MemberReportListResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static com.WalkiePaw.domain.member.entity.QMember.member;
import static com.WalkiePaw.domain.report.entity.QMemberReport.memberReport;
import static org.springframework.util.StringUtils.hasText;

public class MemberReportRepositoryOverrideImpl extends Querydsl4RepositorySupport implements MemberReportRepositoryOverride {

    public MemberReportRepositoryOverrideImpl() {
        super(MemberReport.class);
    }

    @Override
    public Page<MemberReportListResponse> findAllByCond(final String status, Pageable pageable) {
        QMember reported = new QMember("reportedMember");
        QMember reporting = new QMember("reportingMember");
        return page(pageable, page -> page.select(projectionsMemberReportList(reporting, reported))
                        .from(memberReport)
                        .leftJoin(reported).on(member.id.eq(memberReport.reportedMemberId))
                        .leftJoin(reporting).on(member.id.eq(memberReport.reportingMemberId))
                        .where(statusCond(status))
        );
    }

    private static QBean<MemberReportListResponse> projectionsMemberReportList(QMember reporting, QMember reported) {
        return Projections.fields(MemberReportListResponse.class,
                memberReport.id.as("memberReportId"),
                memberReport.title,
                memberReport.reason,
                reporting.name.as("reportingMemberName"),
                reporting.nickname.as("reportingMemberNickname"),
                reported.name.as("reportedMemberName"),
                reported.nickname.as("reportedMemberNickname"),
                memberReport.createdDate
        );
    }

    @Override
    public Page<MemberReportListResponse> findAllWithRelations(Pageable pageable) {
        QMember reported = new QMember("reportedMember");
        QMember reporting = new QMember("reportingMember");
        return page(pageable,
                page -> page.select(projectionsMemberReportList(reporting, reported))
                        .from(memberReport)
                        .leftJoin(reported).on(member.id.eq(memberReport.reportedMemberId))
                        .leftJoin(reporting).on(member.id.eq(memberReport.reportingMemberId))
        );
    }

    @Override
    public MemberReportGetResponse findWithRelationsById(Long memberReportId) {
        QMember reported = new QMember("reportedMember");
        QMember reporting = new QMember("reportingMember");
        return select(Projections.constructor(MemberReportGetResponse.class,
                        memberReport.id.as("memberReportId"),
                        memberReport.reason,
                        reporting.name,
                        reporting.nickname,
                        reported.name,
                        reported.nickname))
                .from(memberReport)
                .leftJoin(reported).on(member.id.eq(memberReport.reportedMemberId))
                .leftJoin(reporting).on(member.id.eq(memberReport.reportingMemberId))
                .where(memberReport.id.eq(memberReportId))
                .fetchFirst();
    }

    private BooleanExpression statusCond(final String status) {
        if (hasText(status)) {
            if (status.equals("RESOLVED")) {
                return memberReport.status.eq(MemberReportStatus.BANNED)
                        .or(memberReport.status.eq(MemberReportStatus.IGNORE));
            } else if (status.equals("UNRESOLVED")) {
                return memberReport.status.eq(MemberReportStatus.UNRESOLVED);
            }
        }
        return null;
    }
}
