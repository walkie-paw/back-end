package com.WalkiePaw.domain.board.repository;

import com.WalkiePaw.domain.board.entity.Board;
import com.WalkiePaw.domain.board.entity.BoardCategory;
import com.WalkiePaw.global.config.JPAConfig;
import com.WalkiePaw.presentation.domain.board.dto.response.BoardListResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 제목·내용 검색(최신 구간 LIKE → Full-Text)이 LIKE 하나로 찾을 때와 같은 글을 같은 순서로 돌려주는지 확인한다.
 *
 * <p>같은 글 묶음을 id 1부터 한 번(오래된 글), id 100,000부터 한 번(최근 글) 넣어, 두 묶음 사이를 LIKE 구간(5만 id)보다 벌린다.
 * 그래서 최근 글만으로 한 페이지가 안 차면 Full-Text 단계까지 가야 오래된 글이 찾아진다.
 *
 * <p>기대값은 Java 의 부분 문자열 검사(대소문자 무시)다. Full-Text 인덱스는 커밋된 행만 보여 테스트 트랜잭션을 쓰지 않는다.
 */
@DataJpaTest(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.properties.hibernate.session_factory.statement_inspector=com.WalkiePaw.domain.board.repository.SqlRecorder"
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@ActiveProfiles("spring-data-jpa")
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class BoardFullTextSearchTest {

    @Container
    @ServiceConnection
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0");

    @SpringBootConfiguration
    @EntityScan("com.WalkiePaw.domain")
    @EnableJpaRepositories("com.WalkiePaw.domain.board.repository")
    @Import(JPAConfig.class)
    static class TestConfig {
    }

    private static final long RECENT_START_ID = 100_000;

    private static final List<String> TEXTS = List.of(
            "강아지 산책 알바 구합니다",
            "강아지산책 해주실 분",
            "골든리트리버 산책",
            "산 책 띄어 쓴 글",
            "산책산책",
            "Walk my dog",
            "WALKING 도우미",
            "C++ 개발자 강아지",
            "a+b 산책",
            "\"따옴표\" 들어간 제목",
            "1:1 산책 (주말)",
            "@home 돌봄",
            "dog_walk 대행",
            "50% 할인 산책",
            "고양이 돌봄",
            "개");

    private static boolean indexCreated;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    DataSource dataSource;

    @BeforeEach
    void setUp() throws Exception {
        if (!indexCreated) {
            try (Connection connection = dataSource.getConnection()) {
                ScriptUtils.executeSqlScript(connection, new ClassPathResource("db/board-fulltext-index.sql"));
            }
            indexCreated = true;
        }
        saveTexts(); // 오래된 글: id 1부터
        new JdbcTemplate(dataSource).execute("alter table board auto_increment = " + RECENT_START_ID);
        saveTexts(); // 최근 글: id 100,000부터
    }

    @AfterEach
    void tearDown() {
        new JdbcTemplate(dataSource).execute("truncate table board");
    }

    @ParameterizedTest
    @ValueSource(strings = {"산책", "강아지 산책", "강아지산책", "리트리버", "walk", "Walk", "C++", "a+b", "따옴표", "\"따옴표\"",
            "산 책", "(주말)", "1:1", "@home", "50%", "50", "책산", "돌봄", "개", "a+산책", "C++ 개발자", "dog_walk", "walk_", "없는검색어"})
    void 제목_검색은_검색어가_들어간_글을_빠짐없이_찾는다(String keyword) {
        assertSameAsSubstringSearch(keyword, Board::getTitle,
                () -> boardRepository.findBySearchCond(keyword, null, BoardCategory.JOB_SEARCH, 100, null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"산책", "강아지 산책", "walk", "C++", "\"따옴표\"", "개"})
    void 내용_검색도_검색어가_들어간_글을_빠짐없이_찾는다(String keyword) {
        assertSameAsSubstringSearch(keyword, Board::getContent,
                () -> boardRepository.findBySearchCond(null, keyword, BoardCategory.JOB_SEARCH, 100, null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"산책", "강아지 산책", "walk", "50%", "@home", "(주말)", "\"따옴표\"", "dog_walk"})
    void 최근_구간에서_한_페이지가_안_차면_Full_Text_인덱스로_찾는다(String keyword) {
        SqlRecorder.clear();
        boardRepository.findBySearchCond(keyword, null, BoardCategory.JOB_SEARCH, 100, null);
        assertThat(SqlRecorder.recorded()).anyMatch(sql -> sql.contains("against("));
    }

    @Test
    void 최근_구간에서_한_페이지가_차면_LIKE_로_끝낸다() {
        SqlRecorder.clear();
        Slice<BoardListResponse> page = boardRepository.findBySearchCond("산책", null, BoardCategory.JOB_SEARCH, 3, null);

        assertThat(page.getContent()).hasSize(3).allMatch(b -> b.getId() >= RECENT_START_ID);
        assertThat(SqlRecorder.recorded()).noneMatch(sql -> sql.contains("against("));
    }

    @ParameterizedTest
    @ValueSource(strings = {"개", "C++", "1:1", "산 책", "a+b", "a+산책"})
    void 한_글자_조각이_있는_검색어는_LIKE_로만_찾는다(String keyword) {
        SqlRecorder.clear();
        boardRepository.findBySearchCond(keyword, null, BoardCategory.JOB_SEARCH, 100, null);
        assertThat(SqlRecorder.recorded()).noneMatch(sql -> sql.contains("against("));
    }

    @Test
    void 커서로_끝까지_넘기면_최근_글과_오래된_글을_id_역순으로_한_번씩_돌려준다() {
        new JdbcTemplate(dataSource).update("update board set created_date = ?", java.time.LocalDateTime.of(2024, 8, 1, 12, 0));
        List<Long> expected = boardRepository.findAll().stream()
                .filter(b -> b.getTitle().contains("산책")).map(Board::getId).sorted(Comparator.reverseOrder()).toList();

        List<Long> seen = new ArrayList<>();
        Long cursor = null;
        Slice<BoardListResponse> slice;
        do {
            slice = boardRepository.findBySearchCond("산책", null, BoardCategory.JOB_SEARCH, 3, cursor);
            slice.getContent().forEach(b -> seen.add(b.getId()));
            cursor = slice.getContent().isEmpty() ? null : slice.getContent().get(slice.getContent().size() - 1).getId();
        } while (slice.hasNext() && seen.size() <= expected.size());

        assertThat(expected).hasSizeGreaterThan(6).anyMatch(id -> id < RECENT_START_ID).anyMatch(id -> id >= RECENT_START_ID);
        assertThat(seen).containsExactlyElementsOf(expected);
    }

    private void saveTexts() {
        boardRepository.saveAll(TEXTS.stream()
                .map(text -> Board.builder()
                        .memberId(1L)
                        .title(text)
                        .content("본문 " + text)
                        .category(BoardCategory.JOB_SEARCH)
                        .build())
                .toList());
    }

    private void assertSameAsSubstringSearch(String keyword, Function<Board, String> field,
                                             Supplier<Slice<BoardListResponse>> search) {
        String needle = keyword.strip().toLowerCase(Locale.ROOT);
        List<Long> expected = boardRepository.findAll().stream()
                .filter(b -> field.apply(b).toLowerCase(Locale.ROOT).contains(needle))
                .map(Board::getId)
                .sorted(Comparator.reverseOrder())
                .toList();

        List<Long> actual = search.get().getContent().stream().map(BoardListResponse::getId).toList();

        assertThat(actual).as("검색어 [%s]", keyword).containsExactlyElementsOf(expected);
    }
}
