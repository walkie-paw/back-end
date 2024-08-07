package com.WalkiePaw.domain.chat.repository;


import com.WalkiePaw.domain.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMsgRepository extends JpaRepository<ChatMessage, Long> {

    @Modifying(clearAutomatically = true)
    @Query("update ChatMessage cm set cm.isRead = true where cm.chatroomId = :chatroomId")
    void bulkIsRead(@Param("chatroomId") Long chatroomId);

    @Query("select cm from ChatMessage cm join Member m on cm.writerId = m.id where cm.chatroomId = :id")
    List<ChatMessage> findWithMemberByChatroomId(@Param("id") Long chatroomId);
}
