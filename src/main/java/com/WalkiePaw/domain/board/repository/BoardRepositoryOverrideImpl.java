package com.WalkiePaw.domain.board.repository;

import com.WalkiePaw.domain.board.entity.Board;
import com.WalkiePaw.domain.board.entity.BoardCategory;
import com.WalkiePaw.domain.board.entity.BoardStatus;
import com.WalkiePaw.global.util.Querydsl4RepositorySupport;
import com.WalkiePaw.presentation.domain.board.dto.response.BoardListResponse;
import com.WalkiePaw.presentation.domain.board.dto.response.BoardMypageListResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.regex.Pattern;

import static com.WalkiePaw.domain.board.entity.QBoard.board;
import static com.WalkiePaw.domain.board.entity.QBoardLike.boardLike;
import static com.WalkiePaw.domain.board.entity.QBoardPhoto.boardPhoto;
import static com.WalkiePaw.domain.member.entity.QMember.member;
import static com.WalkiePaw.global.config.MySqlFullTextFunctionContributor.MATCH_AGAINST;
import static org.springframework.util.StringUtils.hasText;

@Repository
public class BoardRepositoryOverrideImpl extends Querydsl4RepositorySupport implements BoardRepositoryOverride {

    /** MySQL ngram_token_size 기본값 — 이보다 짧은 검색어는 Full-Text 인덱스로 찾을 수 없다 */
    private static final int NGRAM_TOKEN_SIZE = 2;
    /** 검색에서 LIKE 로 먼저 훑는 최신 id 구간 크기 (search 참고) */
    private static final long LIKE_WINDOW = 10_000;
    /** Full-Text 파서가 단어를 끊는 문자(공백·기호) */
    private static final Pattern NON_WORD = Pattern.compile("[^\\p{L}\\p{N}]+");

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
     * <p>정렬을 id 로 맞춘다. id 는 DB 가 INSERT 때 하나씩 매겨(IDENTITY) 겹치지 않으므로, 몰려도 글이 빠지거나 겹치지 않는다.
     * 순서가 작성 시각과 달라지는 것은 거의 동시에 쓴 글끼리뿐이다(번호를 받은 순서와 커밋 순서가 다를 수 있음).
     * 커서 파라미터(id)는 그대로라 API 는 바뀌지 않고, 정렬·범위 조건 모두 PK(클러스터링 인덱스)를 탄다.
     *
     * <p>전제: id 를 DB 한 대가 INSERT 때 매긴다. 앱이 id 범위를 미리 받아 쓰는 시퀀스(pooled 등)나 쓰기 서버 여러 대로 바꾸면
     * id 순서가 작성 시각과 크게 어긋나므로, 그때는 (created_date, id) 복합 커서와 같은 순서의 복합 인덱스로 바꿔야 한다.
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
        return search(title, content, cursor, (keywordCond, rangeCond) -> slice(pageSize, cursor,
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
                        .where(rangeCond)
                        .where(board.status.ne(BoardStatus.DELETED))
                        .where(
                                keywordCond,
                                categoryCond(category))
                        .orderBy(board.id.desc())));
    }

    @Override
    public Slice<BoardListResponse> findBySearchCond(final Long memberId, final String title, final String content, final BoardCategory category, final int pageSize, final Long cursor) {
        return search(title, content, cursor, (keywordCond, rangeCond) -> slice(pageSize, cursor, slice -> slice
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
                .where(rangeCond)
                .where(
                        keywordCond,
                        categoryCond(category))
                .orderBy(board.id.desc())));
    }

    /**
     * 제목·내용 부분 검색을 최신 구간 LIKE → Full-Text 두 단계로 찾는다. 결과와 순서(id 역순)는 LIKE 하나로 찾을 때와 같다.
     *
     * <p>LIKE {@code '%검색어%'} 는 인덱스를 못 타지만 id 역순으로 읽다가 한 페이지를 채우면 멈춰, 흔한 검색어("산책")는 빠르고
     * 드문 검색어는 테이블 전체를 훑는다. Full-Text 인덱스는 반대로 맞는 글을 전부 찾은 뒤 id 순으로 정렬해, 드문 검색어는 빠르고
     * 흔한 검색어는 느리다(100만 건, 첫 페이지: 30% 검색어 LIKE 0.5ms vs Full-Text 약 770ms, 없는 검색어 LIKE 약 560ms vs 0.9ms).
     *
     * <p>그래서 커서 아래 최신 {@value #LIKE_WINDOW}개 id 구간만 LIKE 로 먼저 찾고(구간이 정해져 있어 최대 그만큼만 읽음),
     * 한 페이지를 못 채우면 커서 아래 전체를 Full-Text 로 다시 찾는다. 구간에서 한 페이지가 차는 검색어는 흔한 검색어라
     * LIKE 로 끝나고, 못 채우는 검색어는 맞는 글이 적어 Full-Text 가 빠르다.
     *
     * <p>같은 데이터에서 혼합 방식: 흔한 검색어는 LIKE 와 비슷하고(0.7–3ms), 0.01% 검색어 62 → 8ms, 없는 검색어 560 → 7ms.
     * 구간을 겨우 못 채우는 빈도(0.1%)는 두 단계를 다 거쳐 6.5 → 10ms 로 손해를 본다. 구간 크기는 1만·2만·5만을 재서 골랐다
     * (클수록 손해 구간의 빈도는 낮아지지만 드문 검색어마다 구간 LIKE 비용이 커짐).
     */
    private Slice<BoardListResponse> search(final String title, final String content, final Long cursor,
                                            final BiFunction<BooleanExpression, BooleanExpression, Slice<BoardListResponse>> query) {
        BooleanExpression like = allOf(likeCond(board.title, title), likeCond(board.content, content));
        BooleanExpression fullText = allOf(fullTextCond(board.title, title), fullTextCond(board.content, content));
        if (fullText == null) {
            return query.apply(like, ltBoardId(cursor)); // 검색어가 없거나, 인덱스로 찾을 수 없는 검색어
        }

        Long upper = cursor != null ? cursor : maxBoardIdPlusOne();
        if (upper == null) {
            return query.apply(like, ltBoardId(cursor)); // 글이 없음
        }
        long lower = upper - LIKE_WINDOW;
        Slice<BoardListResponse> recent = query.apply(like, board.id.lt(upper).and(board.id.goe(lower)));
        if (recent.hasNext() || lower <= 1) {
            return recent; // 구간에서 한 페이지가 찼거나, 구간이 이미 커서 아래 전체
        }
        return query.apply(fullText, ltBoardId(cursor));
    }

    private Long maxBoardIdPlusOne() {
        Long max = select(board.id.max()).from(board).fetchOne();
        return max != null ? max + 1 : null;
    }

    private static BooleanExpression allOf(final BooleanExpression a, final BooleanExpression b) {
        if (a == null) {
            return b;
        }
        return b == null ? a : a.and(b);
    }

    private BooleanExpression categoryCond(final BoardCategory category) {
        return category != null ? board.category.eq(category) : null;
    }

    private static BooleanExpression likeCond(final StringPath column, final String keyword) {
        return hasText(keyword) ? column.contains(keyword.strip()) : null; // % _ 는 이스케이프돼 글자 그대로 찾는다
    }

    /**
     * n-gram Full-Text 인덱스(resources/db/board-fulltext-index.sql)로 찾는 조건. 인덱스로 찾을 수 없는 검색어면 null.
     *
     * <p>검색어를 큰따옴표로 감싼 구문(phrase) 검색으로 후보를 찾고, 같은 LIKE 조건으로 한 번 더 걸러 결과를 LIKE 와 똑같이 맞춘다
     * (BoardFullTextSearchTest). 사용자가 입력한 {@code + - * ( ) ~ < > @} 는 따옴표 안이라 연산자가 아니다.
     * 검색어를 공백·기호로 끊었을 때 1글자 조각이 있으면(예: {@code C++}, {@code 1:1}, {@code 산 책}) 2글자 토큰이 안 돼
     * 인덱스로 찾을 수 없다.
     *
     * <p>{@code MATCH(...) > 0} 이 아니라 {@code MATCH(...)} 자체를 조건으로 쓴다(MySqlFullTextFunctionContributor 참고).
     */
    private static BooleanExpression fullTextCond(final StringPath column, final String keyword) {
        if (!hasText(keyword)) {
            return null;
        }
        String phrase = keyword.strip().replace("\"", " ").strip();
        if (!isFullTextSearchable(phrase)) {
            return null;
        }
        return Expressions.booleanTemplate(MATCH_AGAINST + "({0}, {1})", column, "\"" + phrase + "\"")
                .and(likeCond(column, keyword));
    }

    private static boolean isFullTextSearchable(final String phrase) {
        List<String> words = Arrays.stream(NON_WORD.split(phrase)).filter(w -> !w.isEmpty()).toList();
        return !words.isEmpty() && words.stream().allMatch(w -> w.codePointCount(0, w.length()) >= NGRAM_TOKEN_SIZE);
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
