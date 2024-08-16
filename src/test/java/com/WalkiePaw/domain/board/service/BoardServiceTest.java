package com.WalkiePaw.domain.board.service;

import com.WalkiePaw.domain.board.entity.Board;
import com.WalkiePaw.domain.board.repository.BoardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

}