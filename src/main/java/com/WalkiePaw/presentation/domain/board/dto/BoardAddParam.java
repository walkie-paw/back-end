package com.WalkiePaw.presentation.domain.board.dto;

import com.WalkiePaw.domain.board.entity.Board;
import com.WalkiePaw.domain.board.entity.BoardCategory;
import com.WalkiePaw.domain.board.entity.PriceType;
import com.WalkiePaw.presentation.domain.board.dto.request.BoardAddRequest;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class BoardAddParam {

    private final String title;
    private final String content;
    private final int price;
    private final BoardCategory category;
    private final PriceType priceType;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final String location;
    private final String detailedLocation;
    private final boolean priceProposal;
    private final List<ImageDto> images;

    public BoardAddParam(BoardAddRequest request) {
        this.title = request.getTitle();
        this.content = request.getContent();
        this.price = request.getPrice();
        this.category = request.getCategory();
        this.priceType = request.getPriceType();
        this.startTime = request.getStartTime();
        this.endTime = request.getEndTime();
        this.location = request.getLocation();
        this.detailedLocation = request.getDetailedLocation();
        this.priceProposal = request.isPriceProposal();
        this.images = request.getPhotoUrls();
    }

    /**
     * BoardAddResponse -> Board 객체로 만드는 toEntity 메서드 필요
     */
    public static Board toEntity(final BoardAddParam param, final Long memberId) {
        return Board.builder()
                .memberId(memberId)
                .content(param.content)
                .title(param.title)
                .price(param.price)
                .category(param.category)
                .startTime(param.startTime)
                .endTime(param.endTime)
                .priceType(param.priceType)
                .location(param.location)
                .detailedLocation(param.detailedLocation)
                .priceProposal(param.priceProposal)
                .build();
    }
}
