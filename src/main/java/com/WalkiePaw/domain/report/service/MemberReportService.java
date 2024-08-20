package com.WalkiePaw.domain.report.service;

import com.WalkiePaw.domain.member.Repository.MemberRepository;
import com.WalkiePaw.domain.member.entity.Member;
import com.WalkiePaw.domain.report.entity.MemberReport;
import com.WalkiePaw.domain.report.repository.MemberReport.MemberReportRepository;
import com.WalkiePaw.global.exception.BadRequestException;
import com.WalkiePaw.presentation.domain.report.memberReportDto.MemberReportAddParam;
import com.WalkiePaw.presentation.domain.report.memberReportDto.MemberReportUpdateParam;
import com.WalkiePaw.presentation.domain.report.memberReportDto.request.MemberReportUpdateRequest;
import com.WalkiePaw.presentation.domain.report.memberReportDto.response.MemberReportGetResponse;
import com.WalkiePaw.presentation.domain.report.memberReportDto.response.MemberReportListResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static com.WalkiePaw.global.exception.ExceptionCode.NOT_FOUND_MEMBER_ID;
import static com.WalkiePaw.global.exception.ExceptionCode.NOT_FOUND_MEMBER_REPORT_ID;

@Service
@Transactional
@RequiredArgsConstructor
@Validated
public class MemberReportService {

    public static final int REQUIRED_MEMBER_COUNT = 2;
    private final MemberReportRepository memberReportRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public MemberReportGetResponse findById(final @Positive Long memberReportId) {
        boolean exists = memberReportRepository.existsById(memberReportId);
        if (!exists) {
            throw new BadRequestException(NOT_FOUND_MEMBER_REPORT_ID);
        }
        return memberReportRepository.findWithRelationsById(memberReportId);
    }

    @Transactional(readOnly = true)
    public Page<MemberReportListResponse> findAll(final Pageable pageable) {
        return memberReportRepository.findAllWithRelations(pageable);
    }

    public Long save(final @Validated MemberReportAddParam param) {
        Set<Long> idSet = convertIds(param.getReportedMemberId(), param.getReportingMemberId());

        int existsCount = memberRepository.existsByIdIn(idSet);
        if (existsCount != REQUIRED_MEMBER_COUNT) {
            throw new BadRequestException(NOT_FOUND_MEMBER_ID);
        }

        return memberReportRepository.save(MemberReportAddParam.toEntity(param)).getId();
    }

    private static Set<Long> convertIds(final @Positive Long... ids) {
        return Arrays.stream(ids).collect(Collectors.toSet());
    }

    public void update(final @Positive Long memberReportId, final @Validated MemberReportUpdateParam param) {
        Set<Long> idSet = convertIds(param.getReportedMemberId(), param.getReportingMemberId());

        int existsCount = memberRepository.existsByIdIn(idSet);
        if (existsCount != REQUIRED_MEMBER_COUNT) {
            throw new BadRequestException(NOT_FOUND_MEMBER_ID);
        }

        MemberReport memberReport = memberReportRepository.findById(memberReportId).orElseThrow(
                () -> new BadRequestException(NOT_FOUND_MEMBER_REPORT_ID)
        );

        memberReport.update(param.getContent(), param.getReason());
    }

    public void ban(final @Positive Long memberReportId) {
        MemberReport memberReport = memberReportRepository.findById(memberReportId).orElseThrow(
                () -> new BadRequestException(NOT_FOUND_MEMBER_REPORT_ID)
        );
        Member member = memberRepository.findById(memberReport.getReportedMemberId())
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_MEMBER_ID));

        member.ban();
        memberReport.ban();
    }

    public void ignore(final @Positive Long memberReportId) {
        MemberReport memberReport = memberReportRepository.findById(memberReportId).orElseThrow(
                () -> new BadRequestException(NOT_FOUND_MEMBER_REPORT_ID)
        );
        memberReport.ignore();
    }

    public Page<MemberReportListResponse> findAllByCond(final @NotBlank String status, final Pageable pageable) {
        return memberReportRepository.findAllByCond(status, pageable);
    }
}
