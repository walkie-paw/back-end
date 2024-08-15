package com.WalkiePaw.presentation.domain.report.boardReportDto.response;

import com.WalkiePaw.domain.board.entity.Board;
import com.WalkiePaw.domain.member.entity.Member;
import com.WalkiePaw.domain.report.entity.BoardReport;
import com.WalkiePaw.domain.report.entity.BoardReportCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BoardReportGetResponse {
    private String content;
    private BoardReportCategory reason;
    private String reporterName;
    private String boardTitle;
    private String boardWriterName;

    /**
     * Entity -> DTO
     */
    public static BoardReportGetResponse from(BoardReport boardReport, Member writer, Member reporter, Board board) {
        return new BoardReportGetResponse(
                boardReport.getContent(),
                boardReport.getReason(),
                reporter.getName(), // reporterName
                board.getTitle(), // boardWriterName
                writer.getNickname() // boardTitle
        );
    }
}
