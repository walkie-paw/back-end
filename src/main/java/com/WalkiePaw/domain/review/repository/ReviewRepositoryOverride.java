package com.WalkiePaw.domain.review.repository;

import com.WalkiePaw.domain.board.entity.BoardCategory;
import com.WalkiePaw.presentation.domain.review.response.ReviewListResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ReviewRepositoryOverride {
    Slice<ReviewListResponse> findByReviewerIdAndCategory(Pageable pageable, Long reviewerId, BoardCategory category);

    Slice<ReviewListResponse> findByRevieweeIdAndCategory(Pageable pageable, Long revieweeId, BoardCategory category);
}
