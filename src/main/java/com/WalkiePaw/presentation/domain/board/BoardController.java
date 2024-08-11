package com.WalkiePaw.presentation.domain.board;

import com.WalkiePaw.domain.board.entity.BoardCategory;
import com.WalkiePaw.domain.board.service.BoardService;
import com.WalkiePaw.global.aspect.annotation.Trace;
import com.WalkiePaw.presentation.domain.board.dto.BoardAddParam;
import com.WalkiePaw.presentation.domain.board.dto.BoardUpdateParam;
import com.WalkiePaw.presentation.domain.board.request.BoardAddRequest;
import com.WalkiePaw.presentation.domain.board.request.BoardStatusUpdateRequest;
import com.WalkiePaw.presentation.domain.board.request.BoardUpdateRequest;
import com.WalkiePaw.presentation.domain.board.response.BoardGetResponse;
import com.WalkiePaw.presentation.domain.board.response.BoardListResponse;
import com.WalkiePaw.presentation.domain.board.response.BoardMypageListResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/boards")
public class BoardController {

    private final BoardService boardService;
    private static final String BOARD_URL = "/boards/";

    @Trace
    @GetMapping("/list/{category}")
    public ResponseEntity<Slice<BoardListResponse>> getBoardList(
            final @PathVariable BoardCategory category,
            final @RequestParam(required = false) Long memberId,
            Pageable pageable) {
        Slice<BoardListResponse> boardListResponses = boardService.findAllBoardAndMember(memberId, category, pageable);
        return ResponseEntity.ok(boardListResponses);
    }

    @GetMapping("/mypage/{memberId}/{category}")
    public ResponseEntity<Page<BoardMypageListResponse>> mypageList(
            @PathVariable Long memberId,
            @PathVariable BoardCategory category,
            Pageable pageable
    ) {
        Page<BoardMypageListResponse> boards = boardService.findMyBoardsBy(memberId, category, pageable);
        return ResponseEntity.ok(boards);
    }

    @PostMapping
    public ResponseEntity<Void> addBoard(final @Valid @RequestBody BoardAddRequest request) {
        BoardAddParam boardAddParam = new BoardAddParam(request);

        Long saveId = boardService.save(boardAddParam, request.getMemberId());
        return ResponseEntity.created(URI.create(BOARD_URL + saveId)).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardGetResponse> getBoard(final @PathVariable("id") Long boardId) {
        BoardGetResponse response = boardService.getBoard(boardId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateBoard(final @PathVariable("id") Long boardId, final @RequestBody BoardUpdateRequest request) {
        BoardUpdateParam param = new BoardUpdateParam(request);
        boardService.updateBoard(boardId, param);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/status")
    public ResponseEntity<Void> updateBoardStatus(final @RequestBody BoardStatusUpdateRequest request) {
        boardService.updateBoardStatus(request.getBoardId(), request.getStatus());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoard(final @PathVariable("id") Long boardId) {
        boardService.deleteBoard(boardId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Slice<BoardListResponse>> searchBoard(
            final @RequestParam(required = false) Long memberId,
            final @RequestParam(required = false) String title,
            final @RequestParam(required = false) String content,
            final @RequestParam(required = false) BoardCategory category,
            Pageable pageable
    ) {
        Slice<BoardListResponse> list = boardService.findBySearchCond(memberId, title, content, category, pageable);
        return ResponseEntity.ok(list);
    }
}
