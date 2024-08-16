package com.WalkiePaw.presentation.domain.board.dto;

import com.WalkiePaw.domain.board.entity.PriceType;
import com.WalkiePaw.presentation.domain.board.dto.request.BoardUpdateRequest;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BoardUpdateParam {

    private final String title;
    private final String content;
    private final int price;
    private final PriceType priceType;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final String location;
    private final String detailedLocation;
    private final boolean priceProposal;
    private List<ImageDto> photoUrls;

    public BoardUpdateParam(final BoardUpdateRequest request) {
        this.title = request.title();
        this.content = request.content();
        this.price = request.price();
        this.priceType = request.priceType();
        this.startTime = request.startTime();
        this.endTime = request.endTime();
        this.location = request.location();
        this.detailedLocation = request.detailedLocation();
        this.priceProposal = request.priceProposal();
        this.photoUrls = request.photoUrls();
    }
}
