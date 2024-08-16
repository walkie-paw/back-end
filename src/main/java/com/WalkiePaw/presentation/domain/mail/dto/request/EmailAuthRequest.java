package com.WalkiePaw.presentation.domain.mail.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailAuthRequest(@Email String email, @NotBlank String authNum) {
}
