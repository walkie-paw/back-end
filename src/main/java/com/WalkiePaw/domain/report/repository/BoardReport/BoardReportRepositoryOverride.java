package com.WalkiePaw.domain.report.repository.BoardReport;

import com.WalkiePaw.presentation.domain.report.boardReportDto.response.BoardReportListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardReportRepositoryOverride {

    Page<BoardReportListResponse> findAllByResolvedCond(String status, Pageable pageable);

    Page<BoardReportListResponse> findAllWithRelations(Pageable pageable);
}
