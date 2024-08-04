package com.WalkiePaw.domain.board.repository;

import com.WalkiePaw.domain.board.entity.Board;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;

@Profile("spring-data-jpa")
public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryOverride {

    @Query("select b from Board b " +
            "left join Member m on b.memberId = m.id where b.id = :id " +
            "left join BoardPhoto bp on b.id = bp.boardId")
    Optional<Board> getBoardDetail(@Param("id") Long boardId);

    Set<Board> findAllByIdIn(Set<Integer> integers);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Board b SET b.likeCount = :likeCount WHERE b.id = :id")
    int updateLikeCountById(@Param("likeCount") Integer likes, @Param("id") Integer id);
}
