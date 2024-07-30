package com.WalkiePaw.presentation.domain.board.response;

import com.WalkiePaw.domain.board.entity.*;
import com.WalkiePaw.domain.member.entity.Member;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class BoardListResponse {
    private Long id;
    private String title;
    private String content;
    private String location;

    private int price;
    private PriceType priceType;
    private LocalDateTime endTime;
    private LocalDateTime startTime;
    private int likeCount;

    private String memberNickName;
    private BoardStatus status;
    private BoardCategory category;
    private boolean priceProposal;

    private String photoUrls;
    private String memberPhoto;

    private boolean isLiked;

    public BoardListResponse(Long id, String title, String content, String location, int price, PriceType priceType,
                             LocalDateTime endTime, LocalDateTime startTime, int likeCount, String memberNickName,
                             BoardStatus status, BoardCategory category, boolean priceProposal,
                             String memberPhoto, boolean isLiked) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.location = location;
        this.price = price;
        this.priceType = priceType;
        this.endTime = endTime;
        this.startTime = startTime;
        this.likeCount = likeCount;
        this.memberNickName = memberNickName;
        this.status = status;
        this.category = category;
        this.priceProposal = priceProposal;
        this.memberPhoto = memberPhoto;
        this.isLiked = isLiked;
    }

    public BoardListResponse(
            final Long id, final String title, final String content,
            final String location, final int price, final PriceType priceType,
            final LocalDateTime endTime, final LocalDateTime startTime, final int likeCount,
            final String memberNickName, final BoardStatus status, final BoardCategory category,
            final boolean priceProposal, final String photoUrls, final String memberPhoto, final boolean isLiked) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.location = location;
        this.price = price;
        this.priceType = priceType;
        this.endTime = endTime;
        this.startTime = startTime;
        this.likeCount = likeCount;
        this.memberNickName = memberNickName;
        this.status = status;
        this.category = category;
        this.priceProposal = priceProposal;
        this.photoUrls = photoUrls;
        this.memberPhoto = memberPhoto;
        this.isLiked = isLiked;
    }

    public static BoardListResponse from(final Board board, final Member member, final boolean isLiked) {
        return new BoardListResponse(
                board.getId(), board.getTitle(), board.getContent(), board.getLocation(),
                board.getPrice(), board.getPriceType(), board.getEndTime(), board.getStartTime(), board.getLikeCount(),
                member.getNickname(), board.getStatus(),
                board.getCategory(), board.isPriceProposal(), member.getPhoto(), isLiked);
    }

}
