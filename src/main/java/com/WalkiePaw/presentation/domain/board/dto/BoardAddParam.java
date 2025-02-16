package com.WalkiePaw.presentation.domain.board.dto;

import com.WalkiePaw.domain.board.entity.Board;
import com.WalkiePaw.domain.board.entity.BoardCategory;
import com.WalkiePaw.domain.board.entity.PriceType;
import com.WalkiePaw.presentation.domain.board.dto.request.BoardAddRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class BoardAddParam {

    private final @NotBlank String title;
    private final @NotBlank String content;
    private final @PositiveOrZero int price;
    private final @NotNull BoardCategory category;
    private final @NotNull PriceType priceType;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final @NotBlank String location;
    private final @NotBlank String detailedLocation;
    private final boolean priceProposal;
    private final List<ImageDto> images;

    public BoardAddParam(final BoardAddRequest request) {
        this.title = request.title();
        this.content = request.content();
        this.price = request.price();
        this.category = request.category();
        this.priceType = request.priceType();
        this.startTime = request.startTime();
        this.endTime = request.endTime();
        this.location = request.location();
        this.detailedLocation = request.detailedLocation();
        this.priceProposal = request.priceProposal();
        this.images = request.photoUrls();
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
