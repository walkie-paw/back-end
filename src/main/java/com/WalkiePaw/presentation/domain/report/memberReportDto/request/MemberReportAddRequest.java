package com.WalkiePaw.presentation.domain.report.memberReportDto.request;

import com.WalkiePaw.domain.report.entity.MemberReportCategory;

public record MemberReportAddRequest(String content, MemberReportCategory reason, Long reportingMemberId, Long reportedMemberId) {
}
