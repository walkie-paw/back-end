package com.WalkiePaw.presentation.domain.board.dto;

import com.WalkiePaw.domain.board.entity.PriceType;
import com.WalkiePaw.presentation.domain.board.dto.request.BoardUpdateRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class BoardUpdateParam {
    private final @NotBlank String title;
    private final @NotBlank String content;
    private final int price;
    private final @NotNull PriceType priceType;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final @NotBlank String location;
    private final @NotBlank String detailedLocation;
    private final boolean priceProposal;
    private final List<ImageDto> photoUrls;

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
