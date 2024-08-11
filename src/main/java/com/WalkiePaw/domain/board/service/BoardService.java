package com.WalkiePaw.domain.board.service;

import com.WalkiePaw.domain.board.entity.Board;
import com.WalkiePaw.domain.board.entity.BoardCategory;
import com.WalkiePaw.domain.board.entity.BoardPhoto;
import com.WalkiePaw.domain.board.entity.BoardStatus;
import com.WalkiePaw.domain.board.repository.BoardPhotoRepository;
import com.WalkiePaw.domain.board.repository.BoardRepository;
import com.WalkiePaw.domain.member.Repository.MemberRepository;
import com.WalkiePaw.domain.member.entity.Member;
import com.WalkiePaw.global.exception.BadRequestException;
import com.WalkiePaw.presentation.domain.board.ImageDto;
import com.WalkiePaw.presentation.domain.board.dto.BoardAddParam;
import com.WalkiePaw.presentation.domain.board.dto.BoardUpdateParam;
import com.WalkiePaw.presentation.domain.board.response.*;
import com.WalkiePaw.presentation.domain.board.request.BoardStatusUpdateRequest;
import com.WalkiePaw.presentation.domain.board.request.BoardUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.WalkiePaw.global.exception.ExceptionCode.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final BoardPhotoRepository boardPhotoRepository;

    public Slice<BoardListResponse> findAllBoardAndMember(final Long memberId, final BoardCategory category, Pageable pageable) {
        if (memberId == null) {
            return boardRepository.findAllNotDeleted(category, pageable);
        } else {
            return boardRepository.findAllNotDeleted(memberId, category, pageable);
        }
    }

    @Transactional
    public Long save(final BoardAddParam param, final Long memberId) {
        boolean existsMember = memberRepository.existsById(memberId);
        if (!existsMember) {
            throw new BadRequestException(NOT_FOUND_MEMBER_ID);
        }

        Board board = BoardAddParam.toEntity(param, memberId);
        boardRepository.save(board);

        saveBoardPhoto(param.getImages(), board);

        return board.getId();
    }

    private void saveBoardPhoto(final List<ImageDto> request, final Board board) {
        List<BoardPhoto> boardPhotos = request.stream()
                .map(i -> new BoardPhoto(i.getUrl(), board.getId()))
                .toList();
        boardPhotoRepository.saveByIdIn(boardPhotos);
    }


    public BoardGetResponse getBoard(final Long boardId) {
        Board board = boardRepository.getBoardDetail(boardId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_BOARD_ID));

        Member member = memberRepository.findById(board.getMemberId())
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_MEMBER_ID));

        List<BoardPhoto> boardPhotos = boardPhotoRepository.findAllByboardId(boardId);

        return BoardGetResponse.from(board, member, boardPhotos);
    }

    @Transactional
    public void updateBoard(final Long boardId, final BoardUpdateParam param) {
        Board findBoard = boardRepository.findWithPhotoBy(boardId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_BOARD_ID));
        updateFindBoardDetails(param, findBoard);

        Set<String> existingPhotos = boardPhotoRepository.findAllByboardId(boardId).stream()
                .map(BoardPhoto::getUrl)
                .collect(Collectors.toSet());

        Set<String> newPhotos = param.getPhotoUrls().stream()
                .map(ImageDto::getUrl)
                .collect(Collectors.toSet());

        updatePhotosOnBoard(existingPhotos, newPhotos, findBoard);
    }

    private void updatePhotosOnBoard(final Set<String> existingPhotos, final Set<String> newPhotos, final Board findBoard) {
        existingPhotos.removeIf(
                p -> !newPhotos.contains(p));

        List<BoardPhoto> photosToAdd = newPhotos.stream()
                .filter(url -> !existingPhotos.contains(url))
                .map(BoardPhoto::new)
                .toList();

        boardPhotoRepository.saveAll(photosToAdd);
    }

    private static void updateFindBoardDetails(final BoardUpdateParam param, final Board findBoard) {
        findBoard.update(param.getTitle(), param.getContent(), param.getPrice(), param.getStartTime(),
                param.getEndTime(), param.getPriceType(), param.getLocation(), param.getDetailedLocation(), param.isPriceProposal());
    }

    @Transactional
    public void updateBoardStatus(final Long boardId, final BoardStatus status) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_BOARD_ID));
        board.updateStatus(status);
    }

    @Transactional
    public void deleteBoard(final Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_BOARD_ID));
        board.delete();
    }

    public Slice<BoardListResponse> findBySearchCond(
            final Long memberId,
            final String title, final String content, final BoardCategory category, Pageable pageable) {
        if (memberId == null) {
            return boardRepository.findBySearchCond(title, content, category, pageable);
        } else {
            return boardRepository.findBySearchCond(memberId, title, content, category, pageable);
        }
    }

    public Page<BoardMypageListResponse> findMyBoardsBy(final Long memberId, final BoardCategory category, Pageable pageable) {
        return boardRepository.findMyBoardsBy(memberId, category, pageable);
    }
}
