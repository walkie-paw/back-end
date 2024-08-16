package com.WalkiePaw.presentation.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;

public record FindEmailRequest(
        @NotBlank String name,
        @NotBlank String phoneNumber) {
}
