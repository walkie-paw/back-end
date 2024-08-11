package com.WalkiePaw.presentation.domain.review.request;

import com.WalkiePaw.domain.board.entity.BoardCategory;
import com.WalkiePaw.domain.chatroom.entity.Chatroom;
import com.WalkiePaw.domain.member.entity.Member;
import com.WalkiePaw.domain.review.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewSaveRequest {
    private final int point;
    private final String content;
    private final Long chatroomId;
    private final Long reviewerId;
    private final BoardCategory category;

    public static Review toEntity(final ReviewSaveRequest request, Long chatroomId, Long revieweeId, Long reviewerId) {
        return Review.builder()
            .point(request.point)
            .chatroomId(chatroomId)
            .revieweeId(revieweeId)
            .reviewerId(reviewerId)
            .content(request.content)
            .category(request.category)
            .build();
    }
}
