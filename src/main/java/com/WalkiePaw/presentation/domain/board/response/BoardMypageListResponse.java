package com.WalkiePaw.presentation.domain.board.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardMypageListResponse {
    private Long boardId;
    private String title;
    private String content;
    private LocalDateTime createdDate;
}
