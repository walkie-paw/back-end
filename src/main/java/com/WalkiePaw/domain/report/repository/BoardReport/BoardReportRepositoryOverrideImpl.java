package com.WalkiePaw.domain.report.repository.BoardReport;

import com.WalkiePaw.domain.member.entity.QMember;
import com.WalkiePaw.domain.report.entity.BoardReport;
import com.WalkiePaw.domain.report.entity.BoardReportStatus;
import com.WalkiePaw.global.util.Querydsl4RepositorySupport;
import com.WalkiePaw.presentation.domain.report.boardReportDto.response.BoardReportListResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static com.WalkiePaw.domain.board.entity.QBoard.board;
import static com.WalkiePaw.domain.report.entity.QBoardReport.boardReport;
import static org.springframework.util.StringUtils.hasText;

public class BoardReportRepositoryOverrideImpl extends Querydsl4RepositorySupport implements BoardReportRepositoryOverride {

    public BoardReportRepositoryOverrideImpl() {
        super(BoardReport.class);
    }

    @Override
    public Page<BoardReportListResponse> findAllByResolvedCond(String status, Pageable pageable) {
        QMember reporter = new QMember("reporter");
        QMember writer = new QMember("writer");
        return page(pageable,
                page -> page.select(Projections.fields(BoardReportListResponse.class,
                                boardReport.id.as("boardReportId"),
                                boardReport.reason,
                                writer.name.as("writerName"),
                                board.title.as("boardTitle"),
                                writer.name.as("boardWriterName"),
                                writer.nickname.as("boardWriterNickname"),
                                writer.createdDate.as("boardWriterCreatedDate")
                        ))
                        .from(boardReport)
                        .leftJoin(board).on(board.id.eq(boardReport.boardId))
                        .leftJoin(writer).on(writer.id.eq(board.memberId))
                        .leftJoin(reporter).on(reporter.id.eq(boardReport.memberId))
                        .where(statusCond(status)));
    }

    @Override
    public Page<BoardReportListResponse> findAllWithRelations(Pageable pageable) {
        QMember reporter = new QMember("reporter");
        QMember writer = new QMember("writer");
        return page(pageable,
                page -> page.select(Projections.constructor(BoardReportListResponse.class,
                                boardReport.id.as("boardReportId"),
                                boardReport.reason,
                                writer.name.as("writerName"),
                                board.title.as("boardTitle"),
                                writer.name.as("boardWriterName"),
                                writer.nickname.as("boardWriterNickname"),
                                writer.createdDate.as("boardWriterCreatedDate")
                        ))
                        .from(boardReport)
                        .leftJoin(board).on(board.id.eq(boardReport.boardId))
                        .leftJoin(writer).on(writer.id.eq(board.memberId))
                        .leftJoin(reporter).on(reporter.id.eq(boardReport.memberId))
        );
    }

    private BooleanExpression statusCond(final String status) {
        if (hasText(status)) {
            if (status.equals("RESOLVED")) {
                return boardReport.status.eq(BoardReportStatus.BLINDED)
                        .or(boardReport.status.eq(BoardReportStatus.IGNORE));
            } else if (status.equals("UNRESOLVED")) {
                return boardReport.status.eq(BoardReportStatus.UNRESOLVED);
            }
        }
        return null;
    }

}
