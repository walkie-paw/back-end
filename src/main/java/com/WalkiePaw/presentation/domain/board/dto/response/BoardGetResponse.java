package com.WalkiePaw.presentation.domain.board.dto.response;

import com.WalkiePaw.domain.board.entity.*;
import com.WalkiePaw.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class BoardGetResponse {

    private final Long id;
    private final BoardStatus status;
    /**
     * TODO - Member 정보 뭐 넘길지
     */
    private final String detailedLocation;
    private final String location;
    private final String title;
    private final BoardCategory category;
    private final String content;
    private final boolean priceProposal;
    private final int price;
    private final PriceType priceType;
    private final LocalDateTime endTime;
    private final LocalDateTime startTime;
    private final String memberNickName;
    private final List<String> photoUrls;
    private final String memberPhoto;
    private final Long memberId;

    /**
     * BoardGetResponse 생성 메서드
     * @param board Entity
     * @return BoardGetResponse
     */
    public static BoardGetResponse from(final Board board, final Member member, final List<BoardPhoto> boardPhoto) {
        List<String> photoUrls = boardPhoto.stream().map(BoardPhoto::getUrl).toList();
        return new BoardGetResponse(board.getId(), board.getStatus(),
            board.getDetailedLocation(), board.getLocation(), board.getTitle(), board.getCategory(),
            board.getContent(), board.isPriceProposal(),
            board.getPrice(), board.getPriceType(), board.getEndTime(),
            board.getStartTime(), member.getNickname(), photoUrls, member.getPhoto(), member.getId()
        );
    }
}
