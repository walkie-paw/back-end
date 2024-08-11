package com.WalkiePaw.presentation.domain.report.boardReportDto.response;

import com.WalkiePaw.domain.board.entity.Board;
import com.WalkiePaw.domain.member.entity.Member;
import com.WalkiePaw.domain.report.entity.BoardReport;
import com.WalkiePaw.domain.report.entity.BoardReportCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BoardReportListResponse {
    private Long boardReportId;
    private BoardReportCategory reason;
    private String writerName;
    private String boardTitle;
    private String boardWriterName;
    private String boardWriterNickname;
    private LocalDateTime boardWriterCreatedDate;

    /**
     * Entity -> DTO
     * @param boardReport Entity
     * @return DTO
     */
    public static BoardReportListResponse from(BoardReport boardReport, Member reporter, Member boardWriter, Board board) {
        return new BoardReportListResponse(
                boardReport.getId(),
                boardReport.getReason(),
                reporter.getName(), // writerName
                board.getTitle(), // boardTitle
                boardWriter.getName(), // boardWriterName
                boardWriter.getNickname(), // boardWriterNickname
                boardWriter.getCreatedDate() // boardWriterCreatedDate
        );
    }
}
