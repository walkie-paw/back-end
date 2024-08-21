package com.WalkiePaw.presentation.domain.board;

import com.WalkiePaw.domain.board.entity.BoardCategory;
import com.WalkiePaw.domain.board.service.BoardService;
import com.WalkiePaw.global.aspect.annotation.Trace;
import com.WalkiePaw.presentation.domain.board.dto.BoardAddParam;
import com.WalkiePaw.presentation.domain.board.dto.BoardUpdateParam;
import com.WalkiePaw.presentation.domain.board.dto.request.BoardAddRequest;
import com.WalkiePaw.presentation.domain.board.dto.request.BoardStatusUpdateRequest;
import com.WalkiePaw.presentation.domain.board.dto.request.BoardUpdateRequest;
import com.WalkiePaw.presentation.domain.board.dto.response.BoardGetResponse;
import com.WalkiePaw.presentation.domain.board.dto.response.BoardListResponse;
import com.WalkiePaw.presentation.domain.board.dto.response.BoardMypageListResponse;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/boards")
public class BoardController {

    private final BoardService boardService;
    private static final String BOARD_URL = "/api/v1/boards/";

    @Trace
    @GetMapping("/list/{category}")
    @ResponseStatus(OK)
    public Slice<BoardListResponse> getBoardList(
            final @PathVariable BoardCategory category,
            final @RequestParam(required = false) @Positive Long memberId,
            final @RequestParam("page_size") int pageSize,
            final @RequestParam(required = false) Long cursor
    ) {
        return boardService.findAllBoards(memberId, category, pageSize, cursor);
    }

    @GetMapping("/mypage/{memberId}/{category}")
    @ResponseStatus(OK)
    public Page<BoardMypageListResponse> mypageList(
            final @PathVariable @Positive Long memberId,
            final @PathVariable BoardCategory category,
            Pageable pageable
    ) {
        return boardService.findMyBoardsBy(memberId, category, pageable);
    }

    @PostMapping
    public ResponseEntity<Void> addBoard(final @Validated @RequestBody BoardAddRequest request) {
        BoardAddParam boardAddParam = new BoardAddParam(request);
        Long saveId = boardService.save(boardAddParam, request.memberId());
        return ResponseEntity.created(URI.create(BOARD_URL + saveId)).build();
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    public BoardGetResponse getBoard(final @PathVariable("id") @Positive Long boardId) {
        return boardService.getBoard(boardId);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void updateBoard(
            final @PathVariable("id") @Positive Long boardId,
            final @RequestBody @Validated BoardUpdateRequest request
    ) {
        BoardUpdateParam param = new BoardUpdateParam(request);
        boardService.updateBoard(boardId, param);
    }

    @PatchMapping("/status")
    @ResponseStatus(NO_CONTENT)
    public void updateBoardStatus(final @RequestBody @Validated BoardStatusUpdateRequest request) {
        boardService.updateBoardStatus(request.boardId(), request.status());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteBoard(final @PathVariable("id") @Positive Long boardId) {
        boardService.deleteBoard(boardId);
    }

    @GetMapping("/search")
    @ResponseStatus(OK)
    public Slice<BoardListResponse> searchBoard(
            final @RequestParam(required = false) @Positive Long memberId,
            final @RequestParam(required = false) String title,
            final @RequestParam(required = false) String content,
            final @RequestParam(required = false) BoardCategory category,
            final @RequestParam("page_size") int pageSize,
            final @RequestParam(required = false) Long cursor
    ) {
        return boardService.findBySearchCond(memberId, title, content, category, pageSize, cursor);
    }
}
