package com.WalkiePaw.presentation.domain.board.dto.request;

import com.WalkiePaw.domain.board.entity.PriceType;
import com.WalkiePaw.presentation.domain.board.dto.ImageDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;
import java.util.List;

public record BoardUpdateRequest(
        @NotBlank String title,
        @NotBlank String content,
        @Positive int price,
        @NotNull PriceType priceType,
        LocalDateTime startTime,
        LocalDateTime endTime,
        @NotBlank String location,
        @NotBlank String detailedLocation,
        boolean priceProposal,
        List<ImageDto> photoUrls) {
}
