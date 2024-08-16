package com.WalkiePaw.presentation.domain.review.dto;

import com.WalkiePaw.domain.board.entity.BoardCategory;
import com.WalkiePaw.domain.review.entity.Review;
import com.WalkiePaw.presentation.domain.review.dto.request.ReviewSaveRequest;
import jakarta.validation.constraints.*;
import lombok.Getter;

@Getter
public class ReviewSaveParam {
    private final @Positive @Max(5) int point;
    private final @NotBlank String content;
    private final @Positive Long chatroomId;
    private final @Positive Long reviewerId;
    private final @NotNull BoardCategory category;

    public ReviewSaveParam(ReviewSaveRequest request) {
        this.point = request.point();
        this.content = request.content();
        this.chatroomId = request.chatroomId();
        this.reviewerId = request.reviewerId();
        this.category = request.category();
    }

    public static Review toEntity(final ReviewSaveParam param, final Long chatroomId, final Long revieweeId, final Long reviewerId) {
        return Review.builder()
                .point(param.point)
                .chatroomId(chatroomId)
                .revieweeId(revieweeId)
                .reviewerId(reviewerId)
                .content(param.content)
                .category(param.category)
                .build();
    }
}
