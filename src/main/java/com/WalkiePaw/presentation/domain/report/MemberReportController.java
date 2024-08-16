package com.WalkiePaw.presentation.domain.report;

import com.WalkiePaw.domain.report.service.MemberReportService;
import com.WalkiePaw.presentation.domain.report.memberReportDto.MemberReportAddParam;
import com.WalkiePaw.presentation.domain.report.memberReportDto.MemberReportUpdateParam;
import com.WalkiePaw.presentation.domain.report.memberReportDto.request.MemberReportAddRequest;
import com.WalkiePaw.presentation.domain.report.memberReportDto.request.MemberReportUpdateRequest;
import com.WalkiePaw.presentation.domain.report.memberReportDto.response.MemberReportGetResponse;
import com.WalkiePaw.presentation.domain.report.memberReportDto.response.MemberReportListResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/memberReports")
@RequiredArgsConstructor
public class MemberReportController {

    private final MemberReportService memberReportService;
    private static final String MEMBER_REPORT_URL = "/api/v1/memberReports/";

    @GetMapping
    @ResponseStatus(OK)
    public Page<MemberReportListResponse> getMemberReports(final Pageable pageable) {
        return memberReportService.findAll(pageable);
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    public MemberReportGetResponse getMemberReport(final @PathVariable("id") @Positive Long memberReportId) {
        return memberReportService.findById(memberReportId);
    }

    @PostMapping
    public ResponseEntity<Void> addMemberReport(final @Validated @RequestBody MemberReportAddRequest request) {
        var param = new MemberReportAddParam(request);
        Long memberReportId = memberReportService.save(param);
        return ResponseEntity.created(URI.create(MEMBER_REPORT_URL + memberReportId)).build();
    }

    @PatchMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void updateMemberReport(final @PathVariable("id") @Positive Long memberReportId, final @RequestBody @Validated MemberReportUpdateRequest request) {
        var param = new MemberReportUpdateParam(request);
        memberReportService.update(memberReportId, param);
    }

    @PatchMapping("/{id}/ban")
    @ResponseStatus(NO_CONTENT)
    public void banMemberReport(final @PathVariable("id") @Positive Long memberReportId) {
        memberReportService.ban(memberReportId);
    }

    @PatchMapping("/{id}/ignore")
    @ResponseStatus(NO_CONTENT)
    public void ignoreMemberReport(final @PathVariable("id") @Positive Long memberReportId) {
        memberReportService.ignore(memberReportId);
    }

    @GetMapping("/list")
    public Page<MemberReportListResponse> list(
            final @RequestParam(required = false) @NotBlank String status, // RESOLVED, UNRESOLVED
            Pageable pageable
    ) {
        return memberReportService.findAllByCond(status, pageable);
    }

}
