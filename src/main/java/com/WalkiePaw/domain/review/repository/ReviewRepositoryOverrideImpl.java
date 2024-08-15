package com.WalkiePaw.domain.review.repository;

import static com.WalkiePaw.domain.member.entity.QMember.member;
import static com.WalkiePaw.domain.review.entity.QReview.*;

import com.WalkiePaw.domain.board.entity.BoardCategory;
import com.WalkiePaw.domain.review.entity.Review;
import com.WalkiePaw.global.util.Querydsl4RepositorySupport;
import com.WalkiePaw.presentation.domain.review.dto.response.ReviewListResponse;
import com.querydsl.core.types.Projections;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public class ReviewRepositoryOverrideImpl extends Querydsl4RepositorySupport implements ReviewRepositoryOverride {
    public ReviewRepositoryOverrideImpl() {
        super(Review.class);
    }

    @Override
    public Slice<ReviewListResponse> findByReviewerIdAndCategory(Pageable pageable, Long reviewerId, BoardCategory category) {
        return slice(pageable,
                slice -> slice.select(Projections.constructor(ReviewListResponse.class),
                                review.id,
                                review.content,
                                review.point,
                                member.nickname
                        )
                        .from(review)
                        .leftJoin(member).on(review.reviewerId.eq(member.id))
        );
    }

    @Override
    public Slice<ReviewListResponse> findByRevieweeIdAndCategory(Pageable pageable, Long revieweeId, BoardCategory category) {
        return slice(pageable,
                slice -> slice.select(Projections.constructor(ReviewListResponse.class),
                                review.id,
                                review.content,
                                review.point,
                                member.nickname
                        )
                        .from(review)
                        .leftJoin(member).on(review.revieweeId.eq(member.id))
        );
    }
}
