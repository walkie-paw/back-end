package com.WalkiePaw.presentation.domain.member.dto.request;

import java.time.LocalDate;

public record SocialSignUpRequest(String email, String nickname, String phoneNumber, LocalDate birth, String address,
                                  String profile, String photo) {

}
