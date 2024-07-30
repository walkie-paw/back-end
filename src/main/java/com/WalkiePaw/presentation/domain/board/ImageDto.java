package com.WalkiePaw.presentation.domain.board;

import lombok.Data;

@Data
public class ImageDto {
    private String url;

    public ImageDto(final String url) {
        this.url = url;
    }

    public ImageDto() {
    }
}
