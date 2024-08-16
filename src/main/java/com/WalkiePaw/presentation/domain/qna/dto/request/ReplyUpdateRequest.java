package com.WalkiePaw.presentation.domain.qna.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ReplyUpdateRequest(@NotBlank String reply) {
}
