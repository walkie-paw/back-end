package com.WalkiePaw.domain.report.entity;

import com.WalkiePaw.domain.common.BaseEntity;
import com.WalkiePaw.domain.board.entity.Board;
import com.WalkiePaw.domain.member.entity.Member;
import com.WalkiePaw.presentation.domain.report.boardReportDto.request.BoardReportUpdateRequest;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardReport extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_report_id")
    private Long id;
    @Column(name = "board_report_content")
    private String content;
    @Enumerated(EnumType.STRING)
    private BoardReportCategory reason;
    private Long memberId;
    private Long boardId;
    @Enumerated(EnumType.STRING)
    private BoardReportStatus status;

    @Builder
    public BoardReport(String content, Long memberId, Long boardId, BoardReportCategory reason) {
        this.content = content;
        this.memberId = memberId;
        this.boardId = boardId;
        this.reason = reason;
        this.status = BoardReportStatus.UNRESOLVED;
    }

    public void update(BoardReportUpdateRequest request, Long memberId, Long boardId) {
        this.reason = request.getReason();
        this.content = request.getContent();
        this.memberId = memberId;
        this.boardId = boardId;
    }

    public void blind() {
        this.status = BoardReportStatus.BLINDED;
    }

    public void ignore() {
        this.status = BoardReportStatus.IGNORE;
    }
//    /**
//     * BoardReport 생성 메서드
//     */
//    public BoardReport createBoardReport (String title, String content, LocalDate reportedDate, Member member, Board board, BoardReportCategory reason) {
//        return new BoardReport(title, content, reportedDate, member, board, reason);
//    }
}
