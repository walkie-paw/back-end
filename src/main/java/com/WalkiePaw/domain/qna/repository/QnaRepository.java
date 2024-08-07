package com.WalkiePaw.domain.qna.repository;

import com.WalkiePaw.domain.qna.entity.Qna;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

@Profile("spring-data-jpa")
public interface QnaRepository extends JpaRepository<Qna, Long>, QnaRepositoryOverride {

    @Query("select q from Qna q join Member m where q.id = :id")
    Optional<Qna> findWithMemberById(@Param("id") final Long qnaId);

    @Query("select qna from Qna qna join Member m")
    Page<Qna> findAll(Pageable pageable);
}
