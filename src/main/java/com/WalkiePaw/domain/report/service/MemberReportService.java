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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static com.WalkiePaw.global.exception.ExceptionCode.NOT_FOUND_MEMBER_ID;
import static com.WalkiePaw.global.exception.ExceptionCode.NOT_FOUND_MEMBER_REPORT_ID;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberReportService {

    public static final int REQUIRED_MEMBER_COUNT = 2;
    private final MemberReportRepository memberReportRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public MemberReportGetResponse findById(final Long memberReportId) {
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

    public Long save(final MemberReportAddParam param) {
        Set<Long> idSet = convertIds(param.getReportedMemberId(), param.getReportingMemberId());

        int existsCount = memberRepository.existsByIdIn(idSet);
        if (existsCount != REQUIRED_MEMBER_COUNT) {
            throw new BadRequestException(NOT_FOUND_MEMBER_ID);
        }

        return memberReportRepository.save(MemberReportAddParam.toEntity(param)).getId();
    }

    private static Set<Long> convertIds(final Long... ids) {
        return Arrays.stream(ids).collect(Collectors.toSet());
    }

    public void update(final Long memberReportId, final MemberReportUpdateParam param) {
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

    public void ban(final Long memberReportId) {
        MemberReport memberReport = memberReportRepository.findById(memberReportId).orElseThrow(
                () -> new BadRequestException(NOT_FOUND_MEMBER_REPORT_ID)
        );
        Member member = memberRepository.findById(memberReport.getReportedMemberId())
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_MEMBER_ID));

        member.ban();
        memberReport.ban();
    }

    public void ignore(final Long memberReportId) {
        MemberReport memberReport = memberReportRepository.findById(memberReportId).orElseThrow(
                () -> new BadRequestException(NOT_FOUND_MEMBER_REPORT_ID)
        );
        memberReport.ignore();
    }

    public Page<MemberReportListResponse> findAllByCond(final String status, Pageable pageable) {
        return memberReportRepository.findAllByCond(status, pageable);
    }
}
