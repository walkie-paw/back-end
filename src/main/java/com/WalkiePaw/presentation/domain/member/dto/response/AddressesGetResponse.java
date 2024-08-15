package com.WalkiePaw.presentation.domain.member.dto.response;

import com.WalkiePaw.domain.member.entity.Member;

public record AddressesGetResponse(String memberAddress, String selectedAddrs) {
  public static AddressesGetResponse from(Member member) {
    return new AddressesGetResponse(member.getMemberAddress(), member.getSelectedAddresses());
  }
}
