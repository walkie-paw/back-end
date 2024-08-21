package com.WalkiePaw.domain.board.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = @Index(name = "ix_board_photo_board_id", columnList = "board_id"))
public class BoardPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_photo_id")
    private Long id;
    private String url;
    private Long boardId;

    public BoardPhoto(String url, Long boardId) {
        this.boardId = boardId;
        this.url = url;
    }

    public BoardPhoto(final String url) {
        this.url = url;
    }

//    /**
//     * BoardPhoto 생성 메서드
//     */
//    public BoardPhoto createBoardPhoto(String oriName, String uuidName, Board board) {
//        return new BoardPhoto(oriName, uuidName, board);
//    }
}
