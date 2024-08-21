package com.WalkiePaw.domain.board.entity;

import com.WalkiePaw.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = @Index(name = "ix_board_like_board_id", columnList = "board_id"))
public class BoardLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_like_id")
    private Long id;

    private Long memberId;

    private Long boardId;

    public BoardLike(Long memberId, Long boardId) {
        this.memberId = memberId;
        this.boardId = boardId;
    }

//    /**
//     * BoardLike create 메서드
//     */
//    public BoardLike createBoardLike(Member member, Board board) {
//        return new BoardLike(member, board);
//    }
}
