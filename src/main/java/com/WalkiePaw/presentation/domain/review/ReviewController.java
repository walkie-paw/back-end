package com.WalkiePaw.presentation.domain.review;

import com.WalkiePaw.domain.board.entity.BoardCategory;
import com.WalkiePaw.domain.review.service.ReviewService;
import com.WalkiePaw.presentation.domain.review.dto.ReviewSaveParam;
import com.WalkiePaw.presentation.domain.review.dto.request.ReviewSaveRequest;
import com.WalkiePaw.presentation.domain.review.dto.request.ReviewUpdateRequest;
import com.WalkiePaw.presentation.domain.review.dto.response.ReviewDetailResponse;
import com.WalkiePaw.presentation.domain.review.dto.response.ReviewListResponse;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    public static final String REVIEWS_URI = "/api/v1/reviews/";
    private final ReviewService reviewService;

    @GetMapping("/{id}/reviewee")
    @ResponseStatus(OK)
    public Slice<ReviewListResponse> getReviewsByRevieweeId(
            final @RequestParam("page_size") int pageSize,
            final @RequestParam Long cursor,
            final @PathVariable("id") @Positive Long revieweeId,
            final @RequestParam("category") BoardCategory category
    ) {
        return reviewService.findByRevieweeId(pageSize, cursor, revieweeId, category);
    }

    @GetMapping("/{id}/reviewer")
    @ResponseStatus(OK)
    public Slice<ReviewListResponse> getReviewsByReviewerId(
            final @PathVariable("id") @Positive Long reviewerId,
            final @RequestParam("category") BoardCategory category,
            final @RequestParam("page_size") int pageSize,
            final @RequestParam(required = false) Long cursor
    ) {
        return reviewService.findByReviewerId(pageSize, cursor, reviewerId, category);
    }

    @PostMapping
    public ResponseEntity<Void> saveReview(final @RequestBody @Validated ReviewSaveRequest request) {
        ReviewSaveParam param = new ReviewSaveParam(request);
        Long id = reviewService.addReview(param);
        return ResponseEntity.created(URI.create(REVIEWS_URI + id)).build();
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    public ReviewDetailResponse getReview(final @PathVariable @Positive Long id) {
        return reviewService.findById(id);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void updateReview(final @PathVariable @Positive Long id, final @RequestBody @Validated ReviewUpdateRequest request) {
        reviewService.updateReview(id, request.content(), request.point());
    }
    
}
