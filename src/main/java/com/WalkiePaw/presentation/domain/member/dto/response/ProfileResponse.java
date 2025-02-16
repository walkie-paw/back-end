package com.WalkiePaw.presentation.domain.member.dto.response;

import com.WalkiePaw.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProfileResponse {
    private Long memberId;
    private String nickname;
    private String profile;
    private String member_photo;
    private double score;
    private int recruitCount;
    private int researchCount;

    /**
     * Entity -> DTO
     */
    public static ProfileResponse from(Member member) {
        return ProfileResponse.builder()
                .memberId(member.getId())
                .nickname(member.getNickname())
                .profile(member.getProfile())
                .member_photo(member.getPhoto())
                .score(member.getRating())
                .recruitCount(member.getRecruitCnt())
                .researchCount(member.getResearchCnt())
                .build();
    }
}
