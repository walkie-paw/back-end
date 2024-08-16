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
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import static com.WalkiePaw.global.exception.ExceptionCode.NOT_FOUND_MEMBER_ID;

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
                .orElseThrow(() -> new IllegalStateException("잘못된 채팅방 번호입니다."));
        Member reviewer = memberRepository.findById(param.getReviewerId())
                .orElseThrow(() -> new IllegalStateException("잘못된 회원 번호입니다."));

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
                .orElseThrow(() -> new IllegalStateException("잘못된 리뷰 번호입니다."));
        return ReviewDetailResponse.from(review);
    }

    @Transactional
    public void updateReview(final @Positive Long reviewId, final @NotBlank String content, final @Positive @Max(5) int point) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalStateException("잘못된 리뷰 번호입니다."));
        review.update(content, point);
    }

    public Slice<ReviewListResponse> findByReviewerId(Pageable pageable, final @Positive Long reviewerId, final @NotNull BoardCategory category) {
        return reviewRepository.findByReviewerIdAndCategory(pageable, reviewerId, category);
    }

    public Slice<ReviewListResponse> findByRevieweeId(Pageable pageable, final @Positive Long revieweeId, final @NotNull BoardCategory category) {
        return reviewRepository.findByRevieweeIdAndCategory(pageable, revieweeId, category);
    }
}
