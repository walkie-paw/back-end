package com.WalkiePaw.domain.qna.repository;

import com.WalkiePaw.domain.qna.entity.Qna;
import com.WalkiePaw.domain.qna.entity.QnaStatus;
import com.WalkiePaw.global.util.Querydsl4RepositorySupport;
import com.WalkiePaw.presentation.domain.qna.dto.response.QnaListResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static com.WalkiePaw.domain.member.entity.QMember.member;
import static com.WalkiePaw.domain.qna.entity.QQna.qna;
import static org.springframework.util.StringUtils.hasText;

public class QnaRepositoryOverrideImpl extends Querydsl4RepositorySupport implements QnaRepositoryOverride {

    public QnaRepositoryOverrideImpl() {
        super(Qna.class);
    }

    @Override
    public Page<QnaListResponse> findAllByCond(final String status, Pageable pageable) {
        return page(pageable, page -> page.select(
                        Projections.fields(QnaListResponse.class,
                                qna.id.as("qnaId"),
                                qna.memberId.as("memberId"),
                                member.name.as("writerName"),
                                qna.title,
                                qna.status,
                                qna.createdDate,
                                qna.modifiedDate
                        ))
                .from(qna)
                .leftJoin(member).on(qna.memberId.eq(member.id))
                .where(statusCond(status))
                .orderBy(qna.createdDate.desc()));
    }

    @Override
    public Page<QnaListResponse> findByMemberId(final Long memberId, Pageable pageable) {
        return page(pageable, page -> page.select(
                        Projections.fields(QnaListResponse.class,
                                qna.id.as("qnaId"),
                                qna.memberId.as("memberId"),
                                member.name.as("writerName"),
                                qna.title,
                                qna.status,
                                qna.createdDate,
                                qna.modifiedDate
                        ))
                .from(qna)
                .leftJoin(member).on(member.id.eq(memberId))
                .where(qna.memberId.eq(memberId))
                .orderBy(qna.createdDate.desc()));
    }

    private BooleanExpression statusCond(String status) {
        if (hasText(status)) {
            if (status.equals("RESOLVED")) {
                return qna.status.eq(QnaStatus.COMPLETED);
            } else if (status.equals("UNRESOLVED")) {
                return qna.status.eq(QnaStatus.WAITING);
            }
        }
        return null;
    }
}
