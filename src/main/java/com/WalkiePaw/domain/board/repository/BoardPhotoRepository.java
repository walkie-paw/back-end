package com.WalkiePaw.domain.board.repository;

import com.WalkiePaw.domain.board.entity.BoardPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardPhotoRepository extends JpaRepository<BoardPhoto, Long> {
    List<BoardPhoto> findAllByboardId(Long boardId);
}
