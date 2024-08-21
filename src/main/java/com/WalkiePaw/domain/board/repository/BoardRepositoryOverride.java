package com.WalkiePaw.domain.board.repository;

import com.WalkiePaw.domain.board.entity.Board;
import com.WalkiePaw.domain.board.entity.BoardCategory;
import com.WalkiePaw.presentation.domain.board.dto.response.BoardListResponse;
import com.WalkiePaw.presentation.domain.board.dto.response.BoardMypageListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.Optional;

public interface BoardRepositoryOverride {

    Slice<BoardListResponse> findAllNotDeleted(BoardCategory category, int pageSize, Long cursor);

    Slice<BoardListResponse> findAllNotDeleted(Long memberId, BoardCategory category, int pageSize, Long cursor);

    Slice<BoardListResponse> findBySearchCond(Long memberId, String title, String content, BoardCategory category, int pageSize, Long cursor);

    Slice<BoardListResponse> findBySearchCond(String title, String content, BoardCategory category, int pageSize, Long cursor);

    Page<BoardMypageListResponse> findMyBoardsBy(Long memberId, BoardCategory category, Pageable pageable);

    Slice<BoardListResponse> findLikeBoardList(Long memberId, int pageSize, Long cursor);

    Optional<Board> findWithPhotoBy(Long boardId);

}
