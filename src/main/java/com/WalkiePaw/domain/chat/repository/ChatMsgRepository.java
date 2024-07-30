package com.WalkiePaw.domain.chat.repository;


import com.WalkiePaw.domain.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatMsgRepository extends JpaRepository<ChatMessage, Long> {

    @Modifying(clearAutomatically = true)
    @Query("update ChatMessage cm set cm.isRead = true where cm.chatroom.id = :chatroomId")
    void bulkIsRead(Long chatroomId);

    List<ChatMessage> findByChatroomId(Long chatroomId);
}
