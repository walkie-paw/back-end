package com.WalkiePaw.presentation.domain.board.dto;

import com.WalkiePaw.domain.board.entity.PriceType;
import com.WalkiePaw.presentation.domain.board.ImageDto;
import com.WalkiePaw.presentation.domain.board.request.BoardUpdateRequest;
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
        this.title = request.getTitle();
        this.content = request.getContent();
        this.price = request.getPrice();
        this.priceType = request.getPriceType();
        this.startTime = request.getStartTime();
        this.endTime = request.getEndTime();
        this.location = request.getLocation();
        this.detailedLocation = request.getDetailedLocation();
        this.priceProposal = request.isPriceProposal();
        this.photoUrls = request.getPhotoUrls();
    }
}
