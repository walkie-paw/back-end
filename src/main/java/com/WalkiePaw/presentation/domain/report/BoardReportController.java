package com.WalkiePaw.presentation.domain.report;

import com.WalkiePaw.domain.report.service.BoardReportService;
import com.WalkiePaw.presentation.domain.report.boardReportDto.request.BoardReportAddRequest;
import com.WalkiePaw.presentation.domain.report.boardReportDto.request.BoardReportUpdateRequest;
import com.WalkiePaw.presentation.domain.report.boardReportDto.response.BoardReportGetResponse;
import com.WalkiePaw.presentation.domain.report.boardReportDto.response.BoardReportListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/boardReports")
@RequiredArgsConstructor
public class BoardReportController {

    private final BoardReportService boardReportService;
    private static final String BOARD_REPORT_URL = "/boardReports/";

    @GetMapping
    public ResponseEntity<List<BoardReportListResponse>> boardReportList() {
        List<BoardReportListResponse> responses = boardReportService.findAll();
        return ResponseEntity.ok()
                .body(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardReportGetResponse> getBoardReport(final @PathVariable("id") Long boardReportId) {
        BoardReportGetResponse response = boardReportService.findById(boardReportId);
        return ResponseEntity.ok()
                .body(response);
    }

    @PostMapping
    public ResponseEntity<Void> addBoardReport(final @Validated @RequestBody BoardReportAddRequest request) {
        System.out.println("request = " + request);
        Long boardReportId = boardReportService.save(request);
        return ResponseEntity.created(URI.create(BOARD_REPORT_URL + boardReportId)).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateBoardReport(final @PathVariable("id") Long boardReportId, final @Validated @RequestBody BoardReportUpdateRequest request) {
        boardReportService.update(boardReportId, request);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/blind")
    public ResponseEntity<Void> blindBoardReport(final @PathVariable("id") Long boardReportId) {
        boardReportService.blind(boardReportId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/ignore")
    public ResponseEntity<Void> ignoreBoardReport(@PathVariable("id") final Long boardReportId) {
        boardReportService.ignore(boardReportId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/list")
    public ResponseEntity<Page<BoardReportListResponse>> list(
            @RequestParam(required = false) final String status, // RESOLVED, UNRESOLVED
        Pageable pageable
    ) {
        Page<BoardReportListResponse> list = boardReportService.findAllByResolvedCond(status, pageable);
        return ResponseEntity.ok(list);
    }
}
