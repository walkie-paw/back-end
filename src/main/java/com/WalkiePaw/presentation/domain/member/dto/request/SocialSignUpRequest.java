package com.WalkiePaw.presentation.domain.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record SocialSignUpRequest(
        @Email String email,
        @NotBlank String nickname,
        @NotBlank String phoneNumber,
        LocalDate birth,
        @NotBlank String address,
        String profile,
        String photo) {
}
