package com.WalkiePaw.presentation.domain.report;

import com.WalkiePaw.domain.report.service.BoardReportService;
import com.WalkiePaw.presentation.domain.report.boardReportDto.request.BoardReportAddRequest;
import com.WalkiePaw.presentation.domain.report.boardReportDto.request.BoardReportUpdateRequest;
import com.WalkiePaw.presentation.domain.report.boardReportDto.response.BoardReportGetResponse;
import com.WalkiePaw.presentation.domain.report.boardReportDto.response.BoardReportListResponse;
import com.WalkiePaw.presentation.domain.report.boardReportDto.BoardReportAddParam;
import com.WalkiePaw.presentation.domain.report.boardReportDto.BoardReportUpdateParam;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/v1/boardReports")
@RequiredArgsConstructor
public class BoardReportController {

    private final BoardReportService boardReportService;
    private static final String BOARD_REPORT_URL = "/api/v1/boardReports/";

    @GetMapping
    @ResponseStatus(OK)
    public Page<BoardReportListResponse> boardReportList(Pageable pageable) {
        return boardReportService.findAll(pageable);
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    public BoardReportGetResponse getBoardReport(final @PathVariable("id") @Positive Long boardReportId) {
        return boardReportService.findById(boardReportId);
    }

    @PostMapping
    public ResponseEntity<Void> addBoardReport(final @RequestBody @Validated BoardReportAddRequest request) {
        var param = new BoardReportAddParam(request);
        Long boardReportId = boardReportService.save(param);
        return ResponseEntity.created(URI.create(BOARD_REPORT_URL + boardReportId)).build();
    }

    @PatchMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void updateBoardReport(final @PathVariable("id") @Positive Long boardReportId, final @RequestBody @Validated BoardReportUpdateRequest request) {
        var param = new BoardReportUpdateParam(request);
        boardReportService.update(boardReportId, param);
    }

    @PatchMapping("/{id}/blind")
    @ResponseStatus(NO_CONTENT)
    public void blindBoardReport(final @PathVariable("id") @Positive Long boardReportId) {
        boardReportService.blind(boardReportId);
    }

    @PatchMapping("/{id}/ignore")
    @ResponseStatus(NO_CONTENT)
    public void ignoreBoardReport(final @PathVariable("id") @Positive Long boardReportId) {
        boardReportService.ignore(boardReportId);
    }

    @GetMapping("/list")
    @ResponseStatus(OK)
    public Page<BoardReportListResponse> list(
            final @RequestParam(required = false) @NotBlank String status, // RESOLVED, UNRESOLVED
            Pageable pageable
    ) {
        return boardReportService.findAllByResolvedCond(status, pageable);
    }
}
