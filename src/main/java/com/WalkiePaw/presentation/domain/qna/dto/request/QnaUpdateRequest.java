package com.WalkiePaw.presentation.domain.qna.dto.request;

import com.WalkiePaw.domain.qna.entity.QnaStatus;

public record QnaUpdateRequest(Long memberId, String title, String content, String reply, QnaStatus status) {
}
