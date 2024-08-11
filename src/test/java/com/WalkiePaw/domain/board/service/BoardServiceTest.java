package com.WalkiePaw.domain.board.service;

import com.WalkiePaw.domain.board.entity.Board;
import com.WalkiePaw.domain.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {BoardService.class, BoardRepository.class})
class BoardServiceTest {

    @Autowired
    private BoardService boardService;
    @Autowired
    private BoardRepository boardRepository;

    @BeforeEach
    void setUp() {
        for (long i = 0L; i < 100L; i++) {
            Board board = Board.builder().memberId(i).build();
            boardRepository.save(board);
        }
    }

    @Test
    @DisplayName("Board 저장 테스트")
    void saveTest() {
        //given
        for (long i = 0L; i < 100L; i++) {
            Board board = Board.builder().memberId(i).build();
        //when
            boardService.save(board);
        }

        //then
        Assertions.assertThat()
    }

}