package com.WalkiePaw.domain.review.repository;

import com.WalkiePaw.domain.board.entity.BoardCategory;
import com.WalkiePaw.presentation.domain.review.dto.response.ReviewListResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ReviewRepositoryOverride {
    Slice<ReviewListResponse> findByReviewerIdAndCategory(int pageSize, Long cursor, Long reviewerId, BoardCategory category);

    Slice<ReviewListResponse> findByRevieweeIdAndCategory(int pageSize, Long cursor, Long revieweeId, BoardCategory category);
}
