package com.WalkiePaw.presentation.domain.mail.dto.response;

import com.WalkiePaw.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmailAuthResponse {

  private Long memberId;
  private String result;

  /**
   * Entity -> DTO
   */
  public static EmailAuthResponse from(Member member) {
    return EmailAuthResponse.builder()
        .memberId(member.getId())
        .result("Success")
        .build();
  }
}
