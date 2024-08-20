package com.WalkiePaw.domain.review.repository;

import com.WalkiePaw.domain.board.entity.BoardCategory;
import com.WalkiePaw.domain.review.entity.Review;
import com.WalkiePaw.global.util.Querydsl4RepositorySupport;
import com.WalkiePaw.presentation.domain.review.dto.response.ReviewListResponse;
import com.querydsl.core.types.Projections;
import org.springframework.data.domain.Slice;

import static com.WalkiePaw.domain.member.entity.QMember.member;
import static com.WalkiePaw.domain.review.entity.QReview.review;

public class ReviewRepositoryOverrideImpl extends Querydsl4RepositorySupport implements ReviewRepositoryOverride {
    public ReviewRepositoryOverrideImpl() {
        super(Review.class);
    }

    @Override
    public Slice<ReviewListResponse> findByReviewerIdAndCategory(final int pageSize, final Long cursor, final Long reviewerId, final BoardCategory category) {
        return slice(pageSize, cursor,
                slice -> slice.select(Projections.constructor(ReviewListResponse.class),
                                review.id,
                                review.content,
                                review.point,
                                member.nickname
                        )
                        .from(review)
                        .leftJoin(member).on(review.reviewerId.eq(member.id))
                        .where(review.id.lt(cursor))
                        .orderBy(review.id.desc())
        );
    }

    @Override
    public Slice<ReviewListResponse> findByRevieweeIdAndCategory(final int pageSize, final Long cursor, final Long revieweeId, final BoardCategory category) {
        return slice(pageSize, cursor,
                slice -> slice.select(Projections.constructor(ReviewListResponse.class),
                                review.id,
                                review.content,
                                review.point,
                                member.nickname
                        )
                        .from(review)
                        .leftJoin(member).on(review.revieweeId.eq(member.id))
                        .where(review.id.lt(cursor))
                        .orderBy(review.id.desc())
        );
    }
}
