package com.WalkiePaw.domain.report.service;

import com.WalkiePaw.domain.board.entity.Board;
import com.WalkiePaw.domain.board.repository.BoardRepository;
import com.WalkiePaw.domain.member.Repository.MemberRepository;
import com.WalkiePaw.domain.member.entity.Member;
import com.WalkiePaw.domain.report.repository.BoardReportRepository;
import com.WalkiePaw.presentation.domain.report.boardReportDto.BoardReportRequest;
import com.WalkiePaw.presentation.domain.report.boardReportDto.BoardReportResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardReportService {

    private final BoardReportRepository boardReportRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public BoardReportResponse findById(final Integer boardReportId) {
        return BoardReportResponse.from(boardReportRepository.findById(boardReportId));
    }

    @Transactional(readOnly = true)
    public List<BoardReportResponse> findAll() {
        return boardReportRepository.findAll().stream()
                .map(BoardReportResponse::from)
                .toList();
    }

    public Integer save(final BoardReportRequest request) {
        Member member = memberRepository.findById(request.getMemberId());
        Board board = boardRepository.findById(request.getBoardId())
                .orElseThrow();
        return boardReportRepository.save(request.toEntity(member, board));
    }

    public void update(final Integer boardReportId, final BoardReportRequest request) {
        boardReportRepository.update(boardReportId, request);
    }
}
