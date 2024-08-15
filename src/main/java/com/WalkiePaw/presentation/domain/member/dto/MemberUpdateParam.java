package com.WalkiePaw.presentation.domain.member.dto;

import com.WalkiePaw.domain.member.entity.Member;
import com.WalkiePaw.domain.member.entity.MemberStatus;
import com.WalkiePaw.presentation.domain.member.dto.request.MemberUpdateRequest;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class MemberUpdateParam {

    private final String name;
    private final String nickname;
    private final String email;
    private final String phoneNumber;
    private final String address;
    private final LocalDate birth;
    private final String profile;
    private final double rating;
    private final String photo;
    private final MemberStatus status;
    private final int reportedCnt;

    public MemberUpdateParam(MemberUpdateRequest request) {
        this.name = request.name();
        this.nickname = request.nickname();
        this.email = request.email();
        this.phoneNumber = request.phoneNumber();
        this.address = request.address();
        this.birth = request.birth();
        this.profile = request.profile();
        this.rating = request.rating();
        this.photo = request.photo();
        this.status = request.status();
        this.reportedCnt = request.reportedCnt();
    }

    public static Member toEntity(MemberUpdateParam param) {
        return Member.builder()
                .name(param.getName())
                .nickname(param.getNickname())
                .email(param.getEmail())
                .phoneNumber(param.getPhoneNumber())
                .memberAddress(param.getAddress())
                .birth(param.getBirth())
                .profile(param.getProfile())
                .rating(param.getRating())
                .photo(param.getPhoto())
                .build();
    }
}
