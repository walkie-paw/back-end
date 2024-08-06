package com.WalkiePaw.domain.report.repository.BoardReport;

import com.WalkiePaw.domain.report.entity.BoardReport;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Profile("spring-data-jpa")
public interface BoardReportRepository extends JpaRepository<BoardReport, Long>, BoardReportRepositoryOverride {

    @Query("select br from BoardReport br " +
            "join Board b on br.boardId = b.id " +
            "join Member m on m.id = br.memberId " +
            "join Member w on w.id = b.memberId " +
            "where br.id = :brId")
    Optional<BoardReport> findWithAllById(@Param("brId") final Long boardReportId);

    @EntityGraph(attributePaths = {"member", "board"})
    List<BoardReport> findAll();

    @Query("select br from BoardReport br join Board b on br.boardId = b.id where br.id = :id")
    Optional<BoardReport> findWithBoardById(@Param("id") final Long boardReportId);
}
