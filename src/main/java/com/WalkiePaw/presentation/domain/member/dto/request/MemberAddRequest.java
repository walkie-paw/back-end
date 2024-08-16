package com.WalkiePaw.presentation.domain.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

@Schema(description = "멤버 생성 요청 DTO")
public record MemberAddRequest(
        @Schema(description = "이름") @NotBlank String name,
        @Schema(description = "닉네임") @NotBlank String nickname,
        @Schema(description = "이메일") @Email String email,
        @Schema(description = "패스워드") @NotBlank String password,
        @Schema(description = "전화번호") @NotBlank String phoneNumber,
        @Schema(description = "주소") @NotBlank String address,
        @Schema(description = "생년월일") LocalDate birth,
        @Schema(description = "자기소개") String profile,
        @Schema(description = "사진") String photo) {
}
