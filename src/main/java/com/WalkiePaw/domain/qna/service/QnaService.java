package com.WalkiePaw.domain.qna.service;

import com.WalkiePaw.domain.member.Repository.MemberRepository;
import com.WalkiePaw.domain.member.entity.Member;
import com.WalkiePaw.domain.qna.entity.Qna;
import com.WalkiePaw.domain.qna.repository.QnaRepository;
import com.WalkiePaw.global.exception.BadRequestException;
import com.WalkiePaw.presentation.domain.qna.dto.QnaAddParam;
import com.WalkiePaw.presentation.domain.qna.dto.QnaUpdateParam;
import com.WalkiePaw.presentation.domain.qna.dto.request.ReplyUpdateRequest;
import com.WalkiePaw.presentation.domain.qna.dto.response.QnaGetResponse;
import com.WalkiePaw.presentation.domain.qna.dto.response.QnaListResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import static com.WalkiePaw.global.exception.ExceptionCode.NOT_FOUND_MEMBER_ID;
import static com.WalkiePaw.global.exception.ExceptionCode.NOT_FOUND_QNA_ID;

@Service
@Transactional
@Validated
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
    public QnaGetResponse findById(final @Positive Long qnaId) {
        Qna qna = qnaRepository.findWithMemberById(qnaId).orElseThrow(
                () -> new BadRequestException(NOT_FOUND_QNA_ID)
        );
        Member member = memberRepository.findById(qna.getMemberId())
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_MEMBER_ID));
        return QnaGetResponse.from(qna, member);
    }

    public Long save(final @Validated QnaAddParam param) {
        boolean existsById = memberRepository.existsById(param.getMemberId());
        if (!existsById) {
            throw new BadRequestException(NOT_FOUND_MEMBER_ID);
        }
        return qnaRepository.save(QnaAddParam.toEntity(param)).getId();
    }

    public void update(final @Positive Long qnaId, final @Validated QnaUpdateParam param) {
        Qna qna = qnaRepository.findById(qnaId).orElseThrow(
                () -> new BadRequestException(NOT_FOUND_QNA_ID)
        );
        qna.update(param.getTitle(), param.getContent(), param.getReply(), param.getStatus());
    }

    /**
     * TODO - query 확인하기
     */
    public void updateReply(final @Positive Long qnaId, final @NotBlank String reply) {
        qnaRepository.findById(qnaId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_QNA_ID))
                .updateReply(reply);
    }

    public Page<QnaListResponse> findAllByCond(final @NotBlank String status, Pageable pageable) {
        return qnaRepository.findAllByCond(status, pageable);
    }

    public Page<QnaListResponse> findByMemberId(final @Positive Long memberId, Pageable pageable) {
        return qnaRepository.findByMemberId(memberId, pageable);
    }
}
