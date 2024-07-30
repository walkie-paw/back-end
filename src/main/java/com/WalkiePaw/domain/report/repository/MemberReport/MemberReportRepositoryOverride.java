package com.WalkiePaw.domain.report.repository.MemberReport;

import com.WalkiePaw.presentation.domain.report.memberReportDto.response.MemberReportListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberReportRepositoryOverride {

    Page<MemberReportListResponse> findAllByCond(String status, Pageable pageable);
}
