package com.WalkiePaw.presentation.domain.board.request;

import com.WalkiePaw.domain.board.entity.BoardCategory;
import com.WalkiePaw.domain.board.entity.PriceType;
import com.WalkiePaw.presentation.domain.board.ImageDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BoardAddRequest {

    @NotNull
    private final Long memberId;
    @NotBlank
    private final String title;
    @NotBlank
    private final String content;
    // min max 정하기
    private final int price;
    @NotNull
    private final BoardCategory category;
    @NotNull
    private final PriceType priceType;
    // start < end
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;

    private final String location;
    private final String detailedLocation;
    @NotNull
    private final boolean priceProposal;
    private List<ImageDto> photoUrls;

}
