package com.WalkiePaw.domain.review.service;

import com.WalkiePaw.domain.board.entity.BoardCategory;
import com.WalkiePaw.domain.chatroom.entity.Chatroom;
import com.WalkiePaw.domain.chatroom.repository.ChatroomRepository;
import com.WalkiePaw.domain.member.Repository.MemberRepository;
import com.WalkiePaw.domain.member.entity.Member;
import com.WalkiePaw.domain.review.entity.Review;
import com.WalkiePaw.domain.review.repository.ReviewRepository;
import com.WalkiePaw.global.exception.BadRequestException;
import com.WalkiePaw.global.exception.ExceptionCode;
import com.WalkiePaw.presentation.domain.review.response.ReviewDetailResponse;
import com.WalkiePaw.presentation.domain.review.response.ReviewListResponse;
import com.WalkiePaw.presentation.domain.review.request.ReviewSaveRequest;
import com.WalkiePaw.presentation.domain.review.request.ReviewUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.WalkiePaw.global.exception.ExceptionCode.NOT_FOUND_MEMBER_ID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ChatroomRepository chatroomRepository;
    private final MemberRepository memberRepository;


    @Transactional
    public Long addReview(final ReviewSaveRequest request) {
        Chatroom chatroom = chatroomRepository.findWithMemberById(request.getChatroomId(), request.getReviewerId())
                .orElseThrow(() -> new IllegalStateException("잘못된 채팅방 번호입니다."));
        Member reviewer = memberRepository.findById(request.getReviewerId())
                .orElseThrow(() -> new IllegalStateException("잘못된 회원 번호입니다."));

        Review review = null;
        if (chatroom.getSenderId().equals(reviewer.getId())) {
            Member chatroomSender = memberRepository.findById(chatroom.getSenderId())
                    .orElseThrow(() -> new BadRequestException(NOT_FOUND_MEMBER_ID));

            review = ReviewSaveRequest.toEntity(request, chatroom.getId(), chatroomSender.getId(), reviewer.getId());
        } else {
            Member chatroomRecipient = memberRepository.findById(chatroom.getRecipientId())
                    .orElseThrow(() -> new BadRequestException(NOT_FOUND_MEMBER_ID));

            review = ReviewSaveRequest.toEntity(request, chatroom.getId(), chatroomRecipient.getId(), reviewer.getId());
        }

        return reviewRepository.save(review).getId();
    }

    public ReviewDetailResponse findById(final Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalStateException("잘못된 리뷰 번호입니다."));
        return ReviewDetailResponse.from(review);
    }

    @Transactional
    public void updateReview(final Long reviewId, final ReviewUpdateRequest request) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalStateException("잘못된 리뷰 번호입니다."));
        review.update(request.getContent(), request.getPoint());
    }

    public Slice<ReviewListResponse> findByReviewerId(Pageable pageable, final Long reviewerId, final BoardCategory category) {
        return reviewRepository.findByReviewerIdAndCategory(pageable, reviewerId, category);
    }

    public Slice<ReviewListResponse> findByRevieweeId(Pageable pageable, final Long revieweeId, final BoardCategory category) {
        return reviewRepository.findByRevieweeIdAndCategory(pageable, revieweeId, category);
    }
}
