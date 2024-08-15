package com.WalkiePaw.presentation.domain.member.dto.request;

import com.WalkiePaw.domain.member.entity.MemberStatus;

import java.time.LocalDate;

public record MemberUpdateRequest(String name, String nickname, String email, String phoneNumber, String address,
                                  LocalDate birth, String profile, double rating, String photo, MemberStatus status,
                                  int reportedCnt) {
}
