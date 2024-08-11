package com.WalkiePaw.domain.qna.service;

import com.WalkiePaw.domain.member.Repository.MemberRepository;
import com.WalkiePaw.domain.member.entity.Member;
import com.WalkiePaw.domain.qna.entity.Qna;
import com.WalkiePaw.domain.qna.repository.QnaRepository;
import com.WalkiePaw.global.exception.BadRequestException;
import com.WalkiePaw.presentation.domain.qna.response.*;
import com.WalkiePaw.presentation.domain.qna.request.QnaAddRequest;
import com.WalkiePaw.presentation.domain.qna.request.QnaUpdateRequest;
import com.WalkiePaw.presentation.domain.qna.request.ReplyUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.WalkiePaw.global.exception.ExceptionCode.NOT_FOUND_MEMBER_ID;
import static com.WalkiePaw.global.exception.ExceptionCode.NOT_FOUND_QNA_ID;

@Service
@Transactional
@RequiredArgsConstructor
public class QnaService {

    private final QnaRepository qnaRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public Page<QnaListResponse> findAll(final Pageable pageable) {
        return qnaRepository.findAll(pageable).map(q -> QnaListResponse.from(q, memberRepository.findById(q.getMemberId())
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_MEMBER_ID)))
        );
    }

    @Transactional(readOnly = true)
    public QnaGetResponse findById(final Long qnaId) {
        Qna qna = qnaRepository.findWithMemberById(qnaId).orElseThrow(
                () -> new BadRequestException(NOT_FOUND_QNA_ID)
        );
        Member member = memberRepository.findById(qna.getMemberId())
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_MEMBER_ID));
        return QnaGetResponse.from(qna, member);
    }

    public Long save(final QnaAddRequest request) {
        boolean existsById = memberRepository.existsById(request.getMemberId());
        if (!existsById) {
            throw new BadRequestException(NOT_FOUND_MEMBER_ID);
        }
        return qnaRepository.save(QnaAddRequest.toEntity(request)).getId();
    }

    public void update(final Long qnaId, final QnaUpdateRequest request) {
        Qna qna = qnaRepository.findById(qnaId).orElseThrow(
                () -> new BadRequestException(NOT_FOUND_QNA_ID)
        );
        qna.update(request);
    }

    /**
     * TODO - query 확인하기
     */
    public void updateReply(final Long qnaId, final ReplyUpdateRequest request) {
        qnaRepository.existsById(qnaId);
        qnaRepository.findById(qnaId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_QNA_ID))
                .updateReply(request);
    }

    public Page<QnaListResponse> findAllByCond(final String status, Pageable pageable) {
        return qnaRepository.findAllByCond(status, pageable);
    }

    public Page<QnaListResponse> findByMemberId(final Long memberId, Pageable pageable) {
        return qnaRepository.findByMemberId(memberId, pageable);
    }
}
