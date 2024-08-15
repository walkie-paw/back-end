package com.WalkiePaw.domain.board.service;

import com.WalkiePaw.domain.board.entity.Board;
import com.WalkiePaw.domain.board.entity.BoardLike;
import com.WalkiePaw.domain.board.repository.BoardLikeRepository;
import com.WalkiePaw.domain.board.repository.BoardRepository;
import com.WalkiePaw.domain.member.Repository.MemberRepository;
import com.WalkiePaw.global.exception.BadRequestException;
import com.WalkiePaw.presentation.domain.board.dto.response.BoardListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.WalkiePaw.global.exception.ExceptionCode.NOT_FOUND_BOARD_ID;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardLikeService {

    public static final int BOARD_ID_INDEX = 0;
    public static final int LIKE_COUNT = 1;
    public static final int BATCH_SIZE = 50;
    private final BoardLikeRepository boardLikeRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    public Slice<BoardListResponse> findLikeBoardList(final Long memberId, final Pageable pageable) {
        return boardRepository.findLikeBoardList(memberId, pageable);
    }

    public Long saveBoardLike(final Long boardId, final Long loginUserId) {
        boolean existsBoard = boardRepository.existsById(boardId);
        if (!existsBoard) {
            throw new BadRequestException(NOT_FOUND_BOARD_ID);
        }

        boolean existsMember = memberRepository.existsById(loginUserId);
        if (!existsMember) {
            throw new BadRequestException(NOT_FOUND_BOARD_ID);
        }

        BoardLike boardLike = new BoardLike(boardId, loginUserId);

        return boardLikeRepository.save(boardLike).getId();
    }

    public void cancelBoardLike(final Long boardId, final Long loginUserId) {
        BoardLike boardLike = boardLikeRepository.findByMemberIdAndBoardId(loginUserId, boardId);
        boardLikeRepository.delete(boardLike);
    }

    @Scheduled(fixedDelay = 600000)
    public void countBoardLike() {
        Map<Long, Long> counts = boardLikeRepository.countAllBoardLike().stream()
                .collect(Collectors.toMap(
                        c -> c[BOARD_ID_INDEX],
                        c -> c[LIKE_COUNT]
                ));

        Set<Long> batchBoardId = new HashSet<>();
        Set<Board> boards = new HashSet<>();

        for (Long boardId : counts.keySet()) {
            batchBoardId.add(boardId);
            if (batchBoardId.size() == BATCH_SIZE) {
                boards.addAll(boardRepository.findAllByIdIn(batchBoardId));
                batchBoardId.clear();
            }
        }

        if (!batchBoardId.isEmpty()) {
            boards.addAll(boardRepository.findAllByIdIn(batchBoardId));
        }

        boards.forEach(
                b -> b.updateBoardLike(counts.get(b.getId())));

    }
}
