package com.WalkiePaw.domain.review.repository;

import com.WalkiePaw.domain.board.entity.BoardCategory;
import com.WalkiePaw.domain.review.entity.Review;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Profile("spring-data-jpa")
public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryOverride {

    @Query("select r from Review r where r.reviewerId = :reviewer_member_id and r.chatroomId = :chatroom_id")
    Optional<Review> findByReviewerIdAndChatroomId(@Param("reviewer_member_id") Long memberId,@Param("chatroom_id") Long chatroomId);
}
