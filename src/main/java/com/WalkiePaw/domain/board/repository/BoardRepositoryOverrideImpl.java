package com.WalkiePaw.domain.board.repository;

import com.WalkiePaw.domain.board.entity.Board;
import com.WalkiePaw.domain.board.entity.BoardCategory;
import com.WalkiePaw.domain.board.entity.BoardStatus;
import com.WalkiePaw.global.util.Querydsl4RepositorySupport;
import com.WalkiePaw.presentation.domain.board.dto.response.BoardListResponse;
import com.WalkiePaw.presentation.domain.board.dto.response.BoardMypageListResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.WalkiePaw.domain.board.entity.QBoard.board;
import static com.WalkiePaw.domain.board.entity.QBoardLike.boardLike;
import static com.WalkiePaw.domain.board.entity.QBoardPhoto.boardPhoto;
import static com.WalkiePaw.domain.member.entity.QMember.member;
import static org.springframework.util.StringUtils.hasText;

@Repository
public class BoardRepositoryOverrideImpl extends Querydsl4RepositorySupport implements BoardRepositoryOverride {

    public BoardRepositoryOverrideImpl() {
        super(Board.class);
    }

    @Override
    public Slice<BoardListResponse> findAllNotDeleted(final BoardCategory category, final int pageSize, final Long cursor) {
        return slice(pageSize, cursor,
                slice -> slice
                        .select(Projections.constructor(BoardListResponse.class,
                                board.id,
                                board.title,
                                board.content,
                                board.location,
                                board.price,
                                board.priceType,
                                board.endTime,
                                board.startTime,
                                board.likeCount,
                                member.nickname.as("memberNickName"),
                                board.status,
                                board.category,
                                board.priceProposal,
                                getBoardPhoto(),
                                member.photo.as("memberPhoto")))
                        .from(board)
                        .leftJoin(member).on(board.memberId.eq(member.id))
                        .where(ltBoardId(cursor))
                        .where(board.status.ne(BoardStatus.DELETED).and(board.category.eq(category)))
                        .orderBy(board.id.desc()));
    }
    @Override
    public Slice<BoardListResponse> findAllNotDeleted(final Long memberId, final BoardCategory category, final int pageSize, final Long cursor) {
        return slice(pageSize, cursor,
                slice -> slice
                        .select(Projections.constructor(BoardListResponse.class,
                                board.id,
                                board.title,
                                board.content,
                                board.location,
                                board.price,
                                board.priceType,
                                board.endTime,
                                board.startTime,
                                board.likeCount,
                                member.nickname.as("memberNickName"),
                                board.status,
                                board.category,
                                board.priceProposal,
                                getBoardPhoto(),
                                member.photo.as("memberPhoto"),
                                boardLikeQuery(memberId)
                        ))
                        .from(board)
                        .leftJoin(member).on(board.memberId.eq(member.id))
                        .where(ltBoardId(cursor))
                        .where(board.status.ne(BoardStatus.DELETED).and(board.category.eq(category)))
                        .orderBy(board.id.desc()));
    }

    /**
     * 커서 페이징의 커서와 정렬은 같은 키(id)여야 한다.
     *
     * <p>예전에는 커서는 {@code id < 마지막으로 본 id} 인데 정렬은 {@code created_date DESC} 였다. 작성 시각이 같은 글이 있거나,
     * 작성 시각과 id 순서가 뒤바뀐 글(작성 시각은 저장 전에 앱에서, id 는 INSERT 때 정해져 동시 저장 시 뒤바뀔 수 있음)이
     * 페이지 경계에 오면 다음 페이지에서 글이 빠지거나 겹쳤다.
     *
     * <p>정렬을 id 로 맞춘다. {@code created_date} 는 INSERT 때 한 번 정해지고 바뀌지 않아(updatable = false) id 순서와 사실상 같고,
     * id 는 유일해 같은 값이 없다. 커서 파라미터(id)는 그대로라 API 는 바뀌지 않고, 정렬·범위 조건 모두 PK(클러스터링 인덱스)를 탄다.
     */
    private static BooleanExpression ltBoardId(final Long cursor) {
        return cursor != null ? board.id.lt(cursor) : null;
    }

    private static JPQLQuery<String> getBoardPhoto() {
        return JPAExpressions
                .select(boardPhoto.url.min().as("photoUrls"))
                .from(boardPhoto)
                .where(boardPhoto.boardId.eq(board.id));
    }

    private static BooleanExpression boardLikeQuery(final Long memberId) {
        return JPAExpressions.selectOne()
                .from(boardLike)
                .where(boardLike.boardId.eq(board.id)
                        .and(boardLike.memberId.eq(memberId)))
                .exists();
    }


    private static BooleanExpression isLikedCond(final Long memberId) {
        return boardLike.boardId.eq(board.id).and(boardLike.memberId.eq(memberId));
    }

    @Override
    public Slice<BoardListResponse> findBySearchCond(String title, String content, BoardCategory category, final int pageSize, final Long cursor) {
        return slice(pageSize, cursor,
                slice -> slice
                        .select(Projections.constructor(BoardListResponse.class,
                                board.id,
                                board.title,
                                board.content,
                                board.location,
                                board.price,
                                board.priceType,
                                board.endTime,
                                board.startTime,
                                board.likeCount,
                                member.nickname.as("memberNickName"),
                                board.status,
                                board.category,
                                board.priceProposal,
                                getBoardPhoto(),
                                member.photo.as("memberPhoto")))
                        .from(board)
                        .leftJoin(member).on(board.memberId.eq(member.id))
                        .where(ltBoardId(cursor))
                        .where(board.status.ne(BoardStatus.DELETED))
                        .where(
                                titleCond(title),
                                contentCond(content),
                                categoryCond(category))
                        .orderBy(board.id.desc()));
    }

    @Override
    public Slice<BoardListResponse> findBySearchCond(final Long memberId, final String title, final String content, final BoardCategory category, final int pageSize, final Long cursor) {
        return slice(pageSize, cursor, slice -> slice
                .select(Projections.constructor(BoardListResponse.class,
                        board.id,
                        board.title,
                        board.content,
                        board.location,
                        board.price,
                        board.priceType,
                        board.endTime,
                        board.startTime,
                        board.likeCount,
                        member.nickname.as("memberNickName"),
                        board.status,
                        board.category,
                        board.priceProposal,
                        getBoardPhoto(),
                        member.photo.as("memberPhoto"),
                        boardLikeQuery(memberId)
                ))
                .from(board)
                .leftJoin(member).on(board.memberId.eq(member.id))
                .where(ltBoardId(cursor))
                .where(
                        titleCond(title),
                        contentCond(content),
                        categoryCond(category))
                .orderBy(board.id.desc()));
    }

    private BooleanExpression categoryCond(final BoardCategory category) {
        return category != null ? board.category.eq(category) : null;
    }

    private BooleanExpression contentCond(final String content) {
        return hasText(content) ? board.content.like("%" + content + "%") : null;
    }

    private BooleanExpression titleCond(final String title) {
        return hasText(title) ? board.title.like("%" + title + "%") : null;
    }

    @Override
    public Page<BoardMypageListResponse> findMyBoardsBy(final Long memberId, final BoardCategory category, Pageable pageable) {
        return page(pageable, page -> page.select(Projections.fields(BoardMypageListResponse.class,
                        board.id.as("boardId"),
                        board.title,
                        board.content,
                        board.createdDate
                ))
                .from(board)
                .where(board.memberId.eq(memberId).and(board.category.eq(category)))
                .orderBy(board.createdDate.desc()));
    }

    @Override
    public Slice<BoardListResponse> findLikeBoardList(final Long memberId, final int pageSize, final Long cursor) {
        return slice(pageSize, cursor, slice -> slice
                .select(Projections.constructor(BoardListResponse.class,
                        board.id,
                        board.title,
                        board.content,
                        board.location,
                        board.price,
                        board.priceType,
                        board.endTime,
                        board.startTime,
                        board.likeCount,
                        member.nickname.as("memberNickName"),
                        board.status,
                        board.category,
                        board.priceProposal,
                        getBoardPhoto(),
                        member.photo.as("memberPhoto"),
                        boardLikeQuery(memberId)
                ))
                .from(boardLike)
                .leftJoin(board, board).on(boardLike.boardId.eq(board.id))
                .leftJoin(member, member).on(boardLike.memberId.eq(member.id))
                .where(ltBoardId(cursor))
                .where(boardLike.memberId.eq(memberId).and(board.status.ne(BoardStatus.DELETED)))
                .orderBy(board.id.desc()));
    }

    public Optional<Board> findWithPhotoBy(Long boardId) {
        return Optional.ofNullable(
                select(board)
                .from(board)
                .leftJoin(boardPhoto).on(boardPhoto.boardId.eq(boardId))
                .where(board.id.eq(boardId)).fetchOne());
    }
}
