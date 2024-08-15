package com.WalkiePaw.presentation.domain.member.dto;

import com.WalkiePaw.presentation.domain.member.dto.request.SocialSignUpRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
public class SocialSignUpParam {

    private final String email;
    private final String nickname;
    private final String phoneNumber;
    private final LocalDate birth;
    private final String address;
    private final String profile;
    private final String photo;

    public SocialSignUpParam(SocialSignUpRequest request) {
        this.email = request.email();
        this.nickname = request.nickname();
        this.phoneNumber = request.phoneNumber();
        this.birth = request.birth();
        this.address = request.address();
        this.profile = request.profile();
        this.photo = request.photo();
    }
}
