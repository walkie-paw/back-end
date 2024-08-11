package com.WalkiePaw.presentation.domain.board;

import com.WalkiePaw.domain.board.service.BoardLikeService;
import com.WalkiePaw.domain.board.service.BoardService;
import com.WalkiePaw.presentation.domain.board.request.BoardLikeRequest;
import com.WalkiePaw.presentation.domain.board.response.BoardListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/boards-like")
public class BoardLikeController {

    public static final String BOARDS_LIKE = "/boards-like/";
    private final BoardLikeService boardLikeService;

    @GetMapping("/{memberId}")
    public ResponseEntity<Slice<BoardListResponse>> findLikeBoardList(final @PathVariable Long memberId, Pageable pageable) {
        Slice<BoardListResponse> likeBoardList = boardLikeService.findLikeBoardList(memberId, pageable);
        return ResponseEntity.ok(likeBoardList);
    }

    /**
     * TODO - 좋아요 갯수 등 처리
     */
    @PostMapping
    public ResponseEntity<Void> addBoardLike(final @RequestBody BoardLikeRequest request) {
        Long id = boardLikeService.saveBoardLike(request.getBoardId(), request.getLoginUserId());
        return ResponseEntity.created(URI.create(BOARDS_LIKE + id)).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> cancelBoardLike(final @RequestBody BoardLikeRequest request) {
        boardLikeService.cancelBoardLike(request.getBoardId(), request.getLoginUserId());
        return ResponseEntity.noContent().build();
    }
}
