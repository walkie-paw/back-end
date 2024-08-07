package com.WalkiePaw.presentation.domain.review;

import com.WalkiePaw.domain.board.entity.BoardCategory;
import com.WalkiePaw.domain.review.service.ReviewService;
import com.WalkiePaw.presentation.domain.review.response.ReviewDetailResponse;
import com.WalkiePaw.presentation.domain.review.response.ReviewListResponse;
import com.WalkiePaw.presentation.domain.review.request.ReviewSaveRequest;
import com.WalkiePaw.presentation.domain.review.request.ReviewUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    public static final String REVIEWS_URI = "/reviews/";
    private final ReviewService reviewService;

    @GetMapping("/{id}/reviewee")
    public ResponseEntity<Slice<ReviewListResponse>> getReviewsByRevieweeId(
            Pageable pageable,
            @PathVariable("id") final Long revieweeId,
            @RequestParam("category") BoardCategory category
    ) {
        Slice<ReviewListResponse> reviews = reviewService.findByRevieweeId(pageable, revieweeId, category);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/{id}/reviewer")
    public ResponseEntity<Slice<ReviewListResponse>> getReviewsByReviewerId(
            Pageable pageable,
            @PathVariable("id") final Long reviewerId,
            @RequestParam("category") BoardCategory category
    ) {
        Slice<ReviewListResponse> reviews = reviewService.findByReviewerId(pageable, reviewerId, category);
        return ResponseEntity.ok(reviews);
    }

    @PostMapping
    public ResponseEntity<Void> saveReview(final @RequestBody ReviewSaveRequest request) {
        Long id = reviewService.addReview(request);
        return ResponseEntity.created(URI.create(REVIEWS_URI + id)).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewDetailResponse> getReview(final @PathVariable Long id) {
        ReviewDetailResponse response = reviewService.findById(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateReview(final @PathVariable Long id, final @RequestBody ReviewUpdateRequest request) {
        reviewService.updateReview(id, request);
        return ResponseEntity.noContent().build();
    }
    
}
