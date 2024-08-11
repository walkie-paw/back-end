package com.WalkiePaw.domain.member.Repository;

import com.WalkiePaw.domain.member.entity.Member;
import com.WalkiePaw.domain.member.entity.SocialType;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Profile("spring-data-jpa")
public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryOverride {

    @Query("select m from Member m where m.email = :email")
    Optional<Member> findByEmail(@Param("email") String email);

    @Query("select m from Member m where m.nickname = :nickname")
    Optional<Member> findByNickname(@Param("nickname") String nickname);

    Optional<Member> findByEmailAndName(String email, String name);

    Optional<Member> findBySocialTypeAndSocialId(SocialType socialType, String socialId);

    Set<Member> findByIdIn(List<Long> list);

    @Query("select m from Member m join Board b on b.memberId = m.id where m.id = :id")
    Optional<Member> findWithBoardById(@Param("id") final Long memberId);

    @Query("select count(*) from Member m where m.id in :ids")
    int existsByIdIn(@Param("ids") Set<Long> idSet);
}
