package com.WalkiePaw.domain.member.service;

import com.WalkiePaw.domain.mail.service.MailService;
import com.WalkiePaw.domain.member.Repository.MemberRepository;
import com.WalkiePaw.domain.member.entity.Member;
import com.WalkiePaw.global.exception.BadRequestException;
import com.WalkiePaw.presentation.domain.member.dto.MemberAddParam;
import com.WalkiePaw.presentation.domain.member.dto.MemberUpdateParam;
import com.WalkiePaw.presentation.domain.member.dto.SocialSignUpParam;
import com.WalkiePaw.presentation.domain.member.dto.response.*;
import com.WalkiePaw.security.CustomPasswordEncoder;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.WalkiePaw.global.exception.ExceptionCode.*;

@Service
@Transactional
@RequiredArgsConstructor
@Validated
public class MemberService {

    private final MemberRepository memberRepository;
    private final CustomPasswordEncoder passwordEncoder;
    private final MailService mailService;

    @Transactional(readOnly = true)
    public List<MemberListResponse> findAll() {
        List<Member> memberList = memberRepository.findAll();
        return memberList.stream()
                .map(MemberListResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MemberGetResponse findById(final @Positive Long memberId) {
        return MemberGetResponse.from(memberRepository.findById(memberId).orElseThrow(
                () -> new BadRequestException(NOT_FOUND_MEMBER_ID)
        ));
    }

    public Long save(final @Validated MemberAddParam param) {
        Member member = MemberAddParam.toEntity(param);
        passwordEncoder.encodePassword(member);
        return memberRepository.save(member).getId();
    }

    public void update(final @Positive Long id, final @Validated MemberUpdateParam param) {
        Member member = memberRepository.findById(id).orElseThrow(
                () -> new BadRequestException(NOT_FOUND_MEMBER_ID)
        );
        member.updateMember(param);
    }

    @Transactional(readOnly = true)
    public MemberScoreResponse getMemberScore(final @Positive Long memberId) {
        return MemberScoreResponse.from(memberRepository.findById(memberId).orElseThrow(
                () -> new BadRequestException(NOT_FOUND_MEMBER_ID)
        ));
    }

    @Transactional(readOnly = true)
    public MemberRRCountResponse getMemberRRCount(final @Positive Long memberId) {
        return MemberRRCountResponse.from(memberRepository.findById(memberId).orElseThrow(
                () -> new BadRequestException(NOT_FOUND_MEMBER_ID)
        ));
    }

    public void updatePasswd(final @Positive Long memberId, final @NotBlank String password) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new BadRequestException(NOT_FOUND_MEMBER_ID)
        );
        member.updatePasswd(password);
        passwordEncoder.encodePassword(member);
    }

    @Transactional(readOnly = true)
    public Member findByEmail(final @Email String email) {
        return memberRepository.findByEmail(email).orElseThrow(
                () -> new BadRequestException(NOT_FOUND_EMAIL)
        );
    }

    public void draw(final @Positive Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new BadRequestException(NOT_FOUND_MEMBER_ID)
        );
        member.withdraw();
    }

    public void ban(final @Positive Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new BadRequestException(NOT_FOUND_MEMBER_ID)
        );
        member.ban();
    }

    public void general(final @Positive Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new BadRequestException(NOT_FOUND_MEMBER_ID)
        );
        member.general();
    }

    public Page<MemberListResponse> findBySearchCond(
            final String name,
            final String nickname,
            final @Email String email,
            final @Positive Integer reportedCnt,
            final Pageable pageable) {
        return memberRepository.findBySearchCond(name, nickname, email, reportedCnt, pageable);
    }

    public NicknameCheckResponse NicknameCheck(final @NotBlank String nickname) {
        return memberRepository.findByNickname(nickname)
                .map(m -> new NicknameCheckResponse(NCheckResult.DUPLICATED))
                .orElse(new NicknameCheckResponse(NCheckResult.AVAILABLE));
    }


    public FindEmailResponse findEmail(final @NotBlank String name, final @NotBlank String phoneNumber) {
        Member member = memberRepository.findByNameAndPhoneNumber(name, phoneNumber).orElseThrow(
                () -> new BadRequestException(NOT_FOUND_MEMBER_ID)
        );
        return new FindEmailResponse(maskedMail(member.getEmail()));
    }

    public FindPasswdResponse findPasswd(final @Email String email, final @NotBlank String name) {
        Optional<Member> member = memberRepository.findByEmailAndName(email, name);
        if(member.isEmpty()) {
            return new FindPasswdResponse(FindPasswdResult.USER_NOT_FOUND);
        }
        /**
         * TODO
         *  - mail 관련 기능 분리
         */
        Integer authNumber = mailService.makeRandomNumber();
        String setFrom = "no.reply.walkiepaw@gmail.com"; // email-config에 설정한 자신의 이메일 주소를 입력
        String toMail = email;
        String title = "인증 이메일 입니다."; // 이메일 제목
        String content =
                "인증 번호는 " + authNumber + "입니다." +
                        "<br>" +
                        "인증번호를 제대로 입력해주세요"; //이메일 내용 삽입
        mailService.mailSend(setFrom, toMail, title, content);
        return new FindPasswdResponse(FindPasswdResult.SUCCESS);
    }

    private String maskedMail(final @Email String email) {
        int atIndex = email.indexOf('@');
        String beforeAt = email.substring(0, atIndex);
        String afterAt = email.substring(atIndex);
        String visible = beforeAt.substring(0, beforeAt.length() - 4);
        String masked = "*".repeat(4);
        return visible + masked + afterAt;
    }

    public EmailCheckResponse EmailCheck(final @Email String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isEmpty()) {
            return new EmailCheckResponse(EmailCheckResult.AVAILABLE);
        } else {
            return new EmailCheckResponse(EmailCheckResult.DUPLICATED);
        }
    }

    public ProfileResponse findProfile(final @NotBlank String nickanme) {
        return ProfileResponse.from(memberRepository.findByNickname(nickanme).orElseThrow(
                () -> new BadRequestException(NOT_FOUND_NICKNAME)
                )
        );
    }

    public Long socialSignUp(final @Validated SocialSignUpParam param) {
        Member member = memberRepository.findByEmail(param.getEmail()).orElseThrow(
                () -> new BadRequestException(NOT_FOUND_EMAIL)
        );
        return member.updateBySocialSignUpRequest(param);
    }

    public void updateSeletedAddr(final @Positive Long memberId, final @NotBlank String selectedAddresses) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new BadRequestException(NOT_FOUND_MEMBER_ID)
        );
        member.updateSelectedAdrrs(selectedAddresses);
    }

    public AddressesGetResponse getAddressesByMemberId(final @Positive Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(
            () -> new BadRequestException(NOT_FOUND_MEMBER_ID)
        );
        return AddressesGetResponse.from(member);
    }

    public SideBarInfoResponse getSidebarinfoBy(final @Positive Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new BadRequestException(NOT_FOUND_MEMBER_ID)
        );
        return SideBarInfoResponse.from(member);
    }
}
