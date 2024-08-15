package com.WalkiePaw.presentation.domain.member.dto;

import com.WalkiePaw.domain.member.entity.Member;
import com.WalkiePaw.presentation.domain.member.dto.request.MemberAddRequest;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class MemberAddParam {

    private final String name;
    private final String nickname;
    private final String email;
    private final String password;
    private final String phoneNumber;
    private final String address;
    private final LocalDate birth;
    private final String profile;
    private final String photo;

    public MemberAddParam(MemberAddRequest request) {
        this.name = request.name();
        this.nickname = request.nickname();
        this.email = request.email();
        this.password = request.password();
        this.phoneNumber = request.phoneNumber();
        this.address = request.address();
        this.birth = request.birth();
        this.profile = request.profile();
        this.photo = request.photo();
    }

    public static Member toEntity(MemberAddParam param) {
        return Member.builder()
                .name(param.getName())
                .nickname(param.getNickname())
                .email(param.getEmail())
                .password(param.getPassword())
                .phoneNumber(param.phoneNumber)
                .memberAddress(param.getAddress())
                .birth(param.getBirth())
                .profile(param.getProfile())
                .photo(param.getPhoto())
                .build();
    }

}
