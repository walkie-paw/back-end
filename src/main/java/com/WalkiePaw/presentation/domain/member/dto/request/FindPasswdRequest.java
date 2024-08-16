package com.WalkiePaw.presentation.domain.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record FindPasswdRequest (
        @NotBlank String name,
        @Email String email) {
}
