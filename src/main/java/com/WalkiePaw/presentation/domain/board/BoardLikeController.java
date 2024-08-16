package com.WalkiePaw.presentation.domain.board;

import com.WalkiePaw.domain.board.service.BoardLikeService;
import com.WalkiePaw.presentation.domain.board.dto.request.BoardLikeRequest;
import com.WalkiePaw.presentation.domain.board.dto.response.BoardListResponse;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/boards-like")
public class BoardLikeController {

    public static final String BOARDS_LIKE = "/api/v1/boards-like/";
    private final BoardLikeService boardLikeService;

    @GetMapping("/{memberId}")
    @ResponseStatus(OK)
    public Slice<BoardListResponse> findLikeBoardList(final @PathVariable @Positive Long memberId, Pageable pageable) {
        return boardLikeService.findLikeBoardList(memberId, pageable);
    }

    /**
     * TODO - 좋아요 갯수 등 처리
     */
    @PostMapping
    public ResponseEntity<Void> addBoardLike(final @RequestBody @Validated BoardLikeRequest request) {
        Long id = boardLikeService.saveBoardLike(request.boardId(), request.loginUserId());
        return ResponseEntity.created(URI.create(BOARDS_LIKE + id)).build();
    }

    @DeleteMapping
    @ResponseStatus(NO_CONTENT)
    public void cancelBoardLike(final @RequestBody @Validated BoardLikeRequest request) {
        boardLikeService.cancelBoardLike(request.boardId(), request.loginUserId());
    }
}
