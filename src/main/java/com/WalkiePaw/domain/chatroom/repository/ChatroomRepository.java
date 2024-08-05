package com.WalkiePaw.domain.chatroom.repository;

import com.WalkiePaw.domain.chatroom.entity.Chatroom;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Profile("spring-data-jpa")
public interface ChatroomRepository extends JpaRepository<Chatroom, Long>, ChatroomRepositoryOverride {

    List<Chatroom> findByMemberId(final Long memberId);

    @EntityGraph(attributePaths = {"board"})
    Optional<Chatroom> findChatroomAndBoardById(Long chatroomId);

    @Query("select c from Chatroom c join Member m on m.id = :writerId where c.id = :crId")
    Optional<Chatroom> findWithMemberById(@Param("crId") final Long chatroomId, @Param("writerId") final Long writerId);
}
