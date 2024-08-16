package com.WalkiePaw.presentation.domain.member.dto.request;

import com.WalkiePaw.domain.member.entity.MemberStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record MemberUpdateRequest(
        @NotBlank String name,
        @NotBlank String nickname,
        @Email String email,
        @NotBlank String phoneNumber,
        @NotBlank String address,
        LocalDate birth,
        String profile,
        double rating,
        String photo,
        @NotNull MemberStatus status,
        @Positive int reportedCnt) {
}
