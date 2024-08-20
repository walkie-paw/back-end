package com.WalkiePaw.domain.review.service;

import com.WalkiePaw.domain.board.entity.BoardCategory;
import com.WalkiePaw.domain.chatroom.entity.Chatroom;
import com.WalkiePaw.domain.chatroom.repository.ChatroomRepository;
import com.WalkiePaw.domain.member.Repository.MemberRepository;
import com.WalkiePaw.domain.member.entity.Member;
import com.WalkiePaw.domain.review.entity.Review;
import com.WalkiePaw.domain.review.repository.ReviewRepository;
import com.WalkiePaw.global.exception.BadRequestException;
import com.WalkiePaw.presentation.domain.review.dto.ReviewSaveParam;
import com.WalkiePaw.presentation.domain.review.dto.response.ReviewDetailResponse;
import com.WalkiePaw.presentation.domain.review.dto.response.ReviewListResponse;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import static com.WalkiePaw.global.exception.ExceptionCode.*;

@Service
@RequiredArgsConstructor
@Validated
@Transactional(readOnly = true)
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ChatroomRepository chatroomRepository;
    private final MemberRepository memberRepository;


    @Transactional
    public Long addReview(final @Validated ReviewSaveParam param) {
        Chatroom chatroom = chatroomRepository.findWithMemberById(param.getChatroomId(), param.getReviewerId())
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_CHATROOM_ID));
        Member reviewer = memberRepository.findById(param.getReviewerId())
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_MEMBER_ID));

        Review review = null;
        if (chatroom.getSenderId().equals(reviewer.getId())) {
            Member chatroomSender = memberRepository.findById(chatroom.getSenderId())
                    .orElseThrow(() -> new BadRequestException(NOT_FOUND_MEMBER_ID));

            review = ReviewSaveParam.toEntity(param, chatroom.getId(), chatroomSender.getId(), reviewer.getId());
        } else {
            Member chatroomRecipient = memberRepository.findById(chatroom.getRecipientId())
                    .orElseThrow(() -> new BadRequestException(NOT_FOUND_MEMBER_ID));

            review = ReviewSaveParam.toEntity(param, chatroom.getId(), chatroomRecipient.getId(), reviewer.getId());
        }

        return reviewRepository.save(review).getId();
    }

    public ReviewDetailResponse findById(final @Positive Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_REVIEW_ID));
        return ReviewDetailResponse.from(review);
    }

    @Transactional
    public void updateReview(final @Positive Long reviewId, final @NotBlank String content, final @PositiveOrZero @Max(5) int point) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_REVIEW_ID));
        review.update(content, point);
    }

    public Slice<ReviewListResponse> findByReviewerId(final @PositiveOrZero int pageSize, final @Positive Long cursor, final @Positive Long reviewerId, final @NotNull BoardCategory category) {
        return reviewRepository.findByReviewerIdAndCategory(pageSize, cursor, reviewerId, category);
    }

    public Slice<ReviewListResponse> findByRevieweeId(final @PositiveOrZero int pageSize, final @Positive Long cursor, final @Positive Long revieweeId, final @NotNull BoardCategory category) {
        return reviewRepository.findByRevieweeIdAndCategory(pageSize, cursor, revieweeId, category);
    }
}
