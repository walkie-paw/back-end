package com.WalkiePaw.domain.qna.repository;

import com.WalkiePaw.presentation.domain.qna.dto.response.QnaListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QnaRepositoryOverride {

    Page<QnaListResponse> findAllByCond(String status, Pageable pageable);

    Page<QnaListResponse> findByMemberId(Long memberId, Pageable pageable);
}
