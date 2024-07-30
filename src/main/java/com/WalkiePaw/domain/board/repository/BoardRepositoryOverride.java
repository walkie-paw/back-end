package com.WalkiePaw.domain.board.repository;

import com.WalkiePaw.domain.board.entity.Board;
import com.WalkiePaw.domain.board.entity.BoardCategory;
import com.WalkiePaw.presentation.domain.board.response.BoardListResponse;
import com.WalkiePaw.presentation.domain.board.response.BoardMypageListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.Optional;

public interface BoardRepositoryOverride {

    Slice<BoardListResponse> findAllNotDeleted(BoardCategory category, Pageable pageable);

    Slice<BoardListResponse> findBySearchCond(Long memberId, String title, String content, BoardCategory category, Pageable pageable);

    Slice<BoardListResponse> findBySearchCond(String title, String content, BoardCategory category, Pageable pageable);

    Page<BoardMypageListResponse> findMyBoardsBy(Long memberId, BoardCategory category, Pageable pageable);

    Slice<BoardListResponse> findLikeBoardList(Long memberId, Pageable pageable);

    Optional<Board> findWithPhotoBy(Long boardId);

    Slice<BoardListResponse> findAllNotDeleted(Long memberId, BoardCategory category, Pageable pageable);
}
