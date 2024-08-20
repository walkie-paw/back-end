package com.WalkiePaw.domain.report.service;

import com.WalkiePaw.domain.board.entity.Board;
import com.WalkiePaw.domain.board.repository.BoardRepository;
import com.WalkiePaw.domain.member.Repository.MemberRepository;
import com.WalkiePaw.domain.member.entity.Member;
import com.WalkiePaw.domain.report.entity.BoardReport;
import com.WalkiePaw.domain.report.repository.BoardReport.BoardReportRepository;
import com.WalkiePaw.global.exception.BadRequestException;
import com.WalkiePaw.presentation.domain.report.boardReportDto.BoardReportAddParam;
import com.WalkiePaw.presentation.domain.report.boardReportDto.BoardReportUpdateParam;
import com.WalkiePaw.presentation.domain.report.boardReportDto.response.BoardReportGetResponse;
import com.WalkiePaw.presentation.domain.report.boardReportDto.response.BoardReportListResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import static com.WalkiePaw.global.exception.ExceptionCode.*;

@Service
@Transactional
@RequiredArgsConstructor
@Validated
public class BoardReportService {

    private final BoardReportRepository boardReportRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public BoardReportGetResponse findById(final @Positive Long boardReportId) {
        BoardReport boardReport = boardReportRepository.findWithRelations(boardReportId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_BOARD_REPORT_ID));

        Board board = boardRepository.findById(boardReport.getBoardId())
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_BOARD_ID));
        Member reporter = memberRepository.findById(boardReport.getMemberId())
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_MEMBER_ID));
        Member boardWriter = memberRepository.findById(board.getMemberId())
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_MEMBER_ID));

        return BoardReportGetResponse.from(boardReport, boardWriter, reporter, board);
    }

    @Transactional(readOnly = true)
    public Page<BoardReportListResponse> findAll(final Pageable pageable) {
        return boardReportRepository.findAllWithRelations(pageable);
    }

    public Long save(final @Validated BoardReportAddParam param) {
        Member member = memberRepository.findWithBoardById(param.getMemberId()).orElseThrow(
                () -> new BadRequestException(NOT_FOUND_MEMBER_ID)
        );
        Board board = boardRepository.findById(param.getBoardId()).orElseThrow(
                () -> new BadRequestException(NOT_FOUND_BOARD_ID)
        );
        return boardReportRepository.save(BoardReportAddParam.toEntity(param)).getId();
    }

    /**
     * TODO - update 메소드 수정 필요
     */
    public void update(final @Positive Long boardReportId, final @Validated BoardReportUpdateParam param) {
        Member member = memberRepository.findWithBoardById(param.getMemberId()).orElseThrow(
                () -> new BadRequestException(NOT_FOUND_MEMBER_ID)
        );
        Board board = boardRepository.findById(param.getBoardId()).orElseThrow(
                () -> new BadRequestException(NOT_FOUND_BOARD_ID)
        );
        BoardReport boardReport = boardReportRepository.findById(boardReportId).orElseThrow(
                () -> new BadRequestException(NOT_FOUND_BOARD_REPORT_ID)
        );
        boardReport.update(param.getReason(), param.getContent(), member.getId(), board.getId());
    }

    public void blind(final @Positive Long boardReportId) {
        BoardReport boardReport = boardReportRepository.findWithBoardById(boardReportId).orElseThrow(
                () -> new BadRequestException(NOT_FOUND_BOARD_REPORT_ID)
        );
        Board board = boardRepository.findById(boardReport.getBoardId()).orElseThrow(
                () -> new BadRequestException(NOT_FOUND_BOARD_ID)
        );
        board.delete();
        boardReport.blind();
    }

    public void ignore(final @Positive Long boardReportId) {
        BoardReport boardReport = boardReportRepository.findById(boardReportId).orElseThrow(
                () -> new BadRequestException(NOT_FOUND_BOARD_REPORT_ID)
        );
        boardReport.ignore();
    }

    public Page<BoardReportListResponse> findAllByResolvedCond(final @NotBlank String status, final Pageable pageable) {
        return boardReportRepository.findAllByResolvedCond(status, pageable);
    }
}
