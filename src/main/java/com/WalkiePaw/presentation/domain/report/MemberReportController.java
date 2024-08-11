package com.WalkiePaw.presentation.domain.report;

import com.WalkiePaw.domain.report.service.MemberReportService;
import com.WalkiePaw.presentation.domain.report.memberReportDto.request.MemberReportAddRequest;
import com.WalkiePaw.presentation.domain.report.memberReportDto.response.MemberReportGetResponse;
import com.WalkiePaw.presentation.domain.report.memberReportDto.response.MemberReportListResponse;
import com.WalkiePaw.presentation.domain.report.memberReportDto.request.MemberReportUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/memberReports")
@RequiredArgsConstructor
public class MemberReportController {

    private final MemberReportService memberReportService;
    private static final String MEMBER_REPORT_URL = "/memberReports/";

    @GetMapping
    public ResponseEntity<Page<MemberReportListResponse>> getMemberReports(final Pageable pageable) {
        Page<MemberReportListResponse> responses = memberReportService.findAll(pageable);
        return ResponseEntity.ok()
                .body(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberReportGetResponse> getMemberReport(final @PathVariable("id") Long memberReportId) {
        MemberReportGetResponse response = memberReportService.findById(memberReportId);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping
    ResponseEntity<Void> addMemberReport(final @Validated @RequestBody MemberReportAddRequest request) {
        Long memberReportId = memberReportService.save(request);
        return ResponseEntity.created(URI.create(MEMBER_REPORT_URL + memberReportId)).build();
    }

    @PatchMapping("/{id}")
    ResponseEntity<Void> updateMemberReport(final @PathVariable("id") Long memberReportId, final @Validated @RequestBody MemberReportUpdateRequest request) {
        memberReportService.update(memberReportId, request);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/ban")
    ResponseEntity<Void> banMemberReport(@PathVariable("id") final Long memberReportId) {
        memberReportService.ban(memberReportId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/ignore")
    ResponseEntity<Void> ignoreMemberReport(@PathVariable("id") final Long memberReportId) {
        memberReportService.ignore(memberReportId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/list")
    ResponseEntity<Page<MemberReportListResponse>> list(
            @RequestParam(required = false) final String status, // RESOLVED, UNRESOLVED
            Pageable pageable
    ) {
        Page<MemberReportListResponse> list = memberReportService.findAllByCond(status, pageable);
        return ResponseEntity.ok(list);
    }

}
