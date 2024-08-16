package com.WalkiePaw.presentation.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;

public record MemberPasswdUpdateRequest(
        @NotBlank String password) {
}
