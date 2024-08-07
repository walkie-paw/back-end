package com.WalkiePaw.presentation.domain.review.response;

import com.WalkiePaw.domain.member.entity.Member;
import com.WalkiePaw.domain.review.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewListResponse {
    private final Long id;
    private final String content;
    private final int point;
    private final String memberName;


    public static ReviewListResponse from(Review review, Member reviewer) {
        return new ReviewListResponse(review.getId(), review.getContent(), review.getPoint(), reviewer.getNickname());
    }

}
