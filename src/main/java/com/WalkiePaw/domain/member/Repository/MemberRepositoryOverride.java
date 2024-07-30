package com.WalkiePaw.domain.member.Repository;

import com.WalkiePaw.domain.member.entity.Member;
import com.WalkiePaw.presentation.domain.member.response.MemberListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface MemberRepositoryOverride {

    Page<MemberListResponse> findBySearchCond(String name, String nickname, String email, Integer reportedCnt, Pageable pageable);

    Optional<Member> findByNameAndPhoneNumber(String name, String phoneNumber);

    Optional<Member> findByEmailAndNameAndPhoneNumber(String email, String name, String phoneNumber);
}
