package com.WalkiePaw.domain.board.entity;

import com.WalkiePaw.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = @Index(name = "ix_board_member_id", columnList = "member_id"))
public class Board extends BaseEntity {
    public static final String DELETED_MSG = "삭제된 게시글입니다."; // msg 처리 필요

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;
    private Long memberId;
    private String title;
    private String content;
    private int price;
    @Enumerated(EnumType.STRING)
    private PriceType priceType;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private long viewCount;
    private int likeCount;
    private String location;
    private String detailedLocation;
    @Enumerated(EnumType.STRING)
    private BoardStatus status;
    @Enumerated(EnumType.STRING)
    private BoardCategory category;
    private boolean priceProposal;

    @Builder
    public Board(
            Long memberId, String title, String content, int price,
            LocalDateTime startTime, LocalDateTime endTime, PriceType priceType,
            String location, String detailedLocation, BoardCategory category, boolean priceProposal) {
        this.priceType = priceType;
        this.memberId = memberId;
        this.title = title;
        this.content = content;
        this.price = price;
        this.startTime = startTime;
        this.endTime = endTime;
        this.category = category;
        this.detailedLocation = detailedLocation;
        this.location = location;
        this.priceProposal = priceProposal;
        status = BoardStatus.RECRUITING;
    }

    public void updateMember(Long memberId) {
        this.memberId = memberId;
    }

    public void updateTitle(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void update(final String title, final String content, final int price,
                       final LocalDateTime startTime,
                       final LocalDateTime endTime, final PriceType priceType,
                       final String location, final String detailedLocation,
                       final boolean priceProposal) {
        this.title = title;
        this.content = content;
        this.price = price;
        this.startTime = startTime;
        this.endTime = endTime;
        this.priceType = priceType;
        this.location = location;
        this.detailedLocation = detailedLocation;
        this.priceProposal = priceProposal;
    }

    public void delete() {
        this.content = DELETED_MSG;
        this.title = DELETED_MSG;
        this.status = BoardStatus.DELETED;
    }

    public void updateStatus(final BoardStatus status) {
        this.status = status;
    }

    public void updateBoardLike(final int likeCount) {
        this.likeCount = likeCount;
    }


    /**
     * TODO : update 메서드 만들기
     *  - validation 추가
     */
}

