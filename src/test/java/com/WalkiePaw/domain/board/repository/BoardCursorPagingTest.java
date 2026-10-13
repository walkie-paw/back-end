package com.WalkiePaw.domain.board.repository;

import com.WalkiePaw.domain.board.entity.Board;
import com.WalkiePaw.domain.board.entity.BoardCategory;
import com.WalkiePaw.global.config.JPAConfig;
import com.WalkiePaw.presentation.domain.board.dto.response.BoardListResponse;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 무한 스크롤 커서 페이징이 모든 글을 한 번씩만 돌려주는지 MySQL 에서 확인한다.
 *
 * <p>커서는 마지막으로 본 글의 id 다. 정렬 순서가 id 순서와 다르면(작성 시각이 같은 글, 작성 시각과 id 순서가 뒤바뀐 글)
 * 페이지 경계에서 글이 빠지거나 겹친다. 정렬은 DB 마다 다르게 동작할 수 있어 H2 가 아니라 MySQL 컨테이너로 확인한다.
 */
@DataJpaTest(properties = "spring.jpa.hibernate.ddl-auto=create-drop") // 컨테이너 DB 는 내장 DB 가 아니라 기본값이 none
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@ActiveProfiles("spring-data-jpa") // BoardRepository 가 이 프로필에서만 뜬다
class BoardCursorPagingTest {

    @Container
    @ServiceConnection
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0");

    /** 메인 애플리케이션 클래스는 로컬 OAuth 설정 파일을 요구해 테스트에서 띄우지 않는다 */
    @SpringBootConfiguration
    @EntityScan("com.WalkiePaw.domain")
    @EnableJpaRepositories("com.WalkiePaw.domain.board.repository")
    @Import(JPAConfig.class)
    static class TestConfig {
    }

    private static final int PAGE_SIZE = 10;
    private static final int BOARD_COUNT = 25;
    private static final LocalDateTime BASE = LocalDateTime.of(2024, 8, 1, 12, 0);

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    EntityManager em;

    @Test
    void 작성_시각이_같은_글이_있어도_모든_글을_한_번씩_돌려준다() {
        List<Long> ids = saveBoards(BOARD_COUNT);
        ids.forEach(id -> setCreatedDate(id, BASE));

        assertEveryBoardOnce(ids, cursor -> boardRepository.findAllNotDeleted(BoardCategory.JOB_SEARCH, PAGE_SIZE, cursor));
    }

    @Test
    void 작성_시각과_id_순서가_뒤바뀐_글이_페이지_경계에_있어도_빠지지_않는다() {
        // 작성 시각은 저장 전에 앱에서 정해지고 id 는 INSERT 때 정해져, 동시에 저장된 두 글은 순서가 뒤바뀔 수 있다
        List<Long> ids = saveBoards(BOARD_COUNT);
        for (int i = 0; i < ids.size(); i++) {
            setCreatedDate(ids.get(i), BASE.plusSeconds(i));
        }
        // 최신순 10번째 자리에 id 가 더 작은 글이 오도록 15번째·16번째 글의 작성 시각을 맞바꾼다
        setCreatedDate(ids.get(14), BASE.plusSeconds(15));
        setCreatedDate(ids.get(15), BASE.plusSeconds(14));

        assertEveryBoardOnce(ids, cursor -> boardRepository.findAllNotDeleted(BoardCategory.JOB_SEARCH, PAGE_SIZE, cursor));
    }

    // 검색 쿼리의 같은 경우는 Full-Text 인덱스가 필요해 BoardFullTextSearchTest 에서 확인한다

    private void assertEveryBoardOnce(List<Long> savedIds, Function<Long, Slice<BoardListResponse>> page) {
        List<Long> seen = new ArrayList<>();
        Long cursor = null;
        Slice<BoardListResponse> slice;
        do {
            slice = page.apply(cursor);
            slice.getContent().forEach(b -> seen.add(b.getId()));
            cursor = slice.getContent().isEmpty() ? null : slice.getContent().get(slice.getContent().size() - 1).getId();
        } while (slice.hasNext() && seen.size() <= savedIds.size());

        assertThat(seen).as("페이지를 끝까지 넘겨 받은 글 id").containsExactlyInAnyOrderElementsOf(savedIds);
        assertThat(seen).as("최신 글(큰 id)부터").isSortedAccordingTo((a, b) -> Long.compare(b, a));
    }

    private List<Long> saveBoards(int count) {
        List<Long> ids = LongStream.range(0, count)
                .mapToObj(i -> {
                    Board board = Board.builder()
                            .memberId(1L)
                            .title("산책 " + i)
                            .content("내용 " + i)
                            .category(BoardCategory.JOB_SEARCH)
                            .build();
                    em.persist(board);
                    return board.getId();
                })
                .toList();
        em.flush();
        return ids;
    }

    private void setCreatedDate(Long id, LocalDateTime createdDate) {
        em.createNativeQuery("update board set created_date = :createdDate where board_id = :id")
                .setParameter("createdDate", createdDate)
                .setParameter("id", id)
                .executeUpdate();
        em.clear();
    }
}
