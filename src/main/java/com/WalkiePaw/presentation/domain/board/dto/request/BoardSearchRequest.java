package com.WalkiePaw.presentation.domain.board.dto.request;

import com.WalkiePaw.domain.board.entity.BoardCategory;


public record BoardSearchRequest(String title, String content, BoardCategory category) {
}
