package com.WalkiePaw.domain.chat.service;

import com.WalkiePaw.domain.chat.entity.ChatMessage;
import com.WalkiePaw.domain.chat.repository.ChatMsgRepository;
import com.WalkiePaw.domain.chatroom.entity.Chatroom;
import com.WalkiePaw.domain.chatroom.repository.ChatroomRepository;
import com.WalkiePaw.domain.member.Repository.MemberRepository;
import com.WalkiePaw.domain.member.entity.Member;
import com.WalkiePaw.global.exception.BadRequestException;
import com.WalkiePaw.presentation.domain.chat.dto.ChatAddParam;
import com.WalkiePaw.presentation.domain.chat.dto.request.ChatAddRequest;
import com.WalkiePaw.presentation.domain.chat.dto.response.ChatMsgListResponse;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Set;

import static com.WalkiePaw.global.exception.ExceptionCode.NOT_FOUND_CHATROOM_ID;
import static com.WalkiePaw.global.exception.ExceptionCode.NOT_FOUND_MEMBER_ID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Validated
public class ChatService {

    private final ChatMsgRepository chatMsgRepository;
    private final ChatroomRepository chatroomRepository;
    private final MemberRepository memberRepository;

    public List<ChatMsgListResponse> findChatsByChatroomId(final @Positive Long chatroomId) {
        List<ChatMessage> chatMessagesList = chatMsgRepository.findWithMemberByChatroomId(chatroomId);
        Set<Member> members = memberRepository.findByIdIn(chatMessagesList.stream().map(ChatMessage::getWriterId).toList());
        return chatMessagesList.stream()
                .map(
                        cm -> {
                            Member writer = members.stream().filter(m -> m.getId().equals(cm.getWriterId())).findAny()
                                    .orElseThrow(() -> new BadRequestException(NOT_FOUND_MEMBER_ID));
                            return ChatMsgListResponse.from(cm, writer.getNickname());
                        }
                )
                .toList();
    }

    @Transactional
    public ChatMsgListResponse saveChatMsg(final @Positive Long chatroomId, final @Validated ChatAddParam param) {
        Chatroom chatroom = chatroomRepository.findWithMemberById(chatroomId, param.getWriterId())
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_CHATROOM_ID));
        Member member = memberRepository.findById(param.getWriterId())
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_MEMBER_ID));
        chatroom.updateLatestMessage(param.getContent());
        ChatMessage chatMsg = param.toEntity(param, chatroomId);
        return ChatMsgListResponse.from(chatMsgRepository.save(chatMsg), param.getNickname());
    }

    public void bulkUpdateIsRead(final Long chatroomId) {
        chatMsgRepository.bulkIsRead(chatroomId);
    }
}
