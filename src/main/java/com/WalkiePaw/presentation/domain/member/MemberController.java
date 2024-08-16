package com.WalkiePaw.presentation.domain.member;

import com.WalkiePaw.domain.member.service.MemberService;
import com.WalkiePaw.presentation.domain.member.dto.MemberUpdateParam;
import com.WalkiePaw.presentation.domain.member.dto.MemberAddParam;
import com.WalkiePaw.presentation.domain.member.dto.SocialSignUpParam;
import com.WalkiePaw.presentation.domain.member.dto.request.*;
import com.WalkiePaw.presentation.domain.member.dto.response.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@Tag(name = "members", description = "멤버 API")
@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private static final String MEMBER_URL = "/api/v1/members/";

    @ApiResponse(responseCode = "200")
    @Operation(summary = "멤버 리스트")
    @GetMapping
    @ResponseStatus(OK)
    public List<MemberListResponse> memberList() {
        return memberService.findAll();
    }

    @ApiResponse(responseCode = "200")
    @Operation(summary = "멤버 조회")
    @GetMapping("/{id}")
    @ResponseStatus(OK)
    public MemberGetResponse getMember(final @Parameter(description = "멤버의 ID") @PathVariable("id") @Positive Long memberId) {
        return memberService.findById(memberId);
    }

    @ApiResponse(responseCode = "201")
    @Operation(summary = "멤버 추가")
    @PostMapping
    public ResponseEntity<Void> addMember(final @RequestBody @Validated MemberAddRequest request) {
        var param = new MemberAddParam(request);
        Long memberId = memberService.save(param);
        return ResponseEntity.created(URI.create(MEMBER_URL + memberId)).build();
    }

    @Operation(summary = "소셜로그인 회원가입")
    @PostMapping("/social-signup")
    public ResponseEntity<Void> socialSignUp(final @RequestBody @Validated SocialSignUpRequest request) {
        var param = new SocialSignUpParam(request);
        Long memberId = memberService.socialSignUp(param);
        return ResponseEntity.created(URI.create(MEMBER_URL + memberId)).build();
    }

    @ApiResponse(responseCode = "204", description = "수정됨")
    @Operation(summary = "멤버 수정")
    @PatchMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void updateMember(final @PathVariable("id") @Positive Long memberId,
                             final @RequestBody @Validated MemberUpdateRequest request) {
        var param = new MemberUpdateParam(request);
        memberService.update(memberId, param);
    }

    @Operation(summary = "멤버 리뷰 평점 조회")
    @GetMapping("/{id}/score")
    @ResponseStatus(OK)
    public MemberScoreResponse getMemberScore(final @PathVariable("id") @Positive Long memberId) {
        return memberService.getMemberScore(memberId);
    }

    @Operation(summary = "멤버 구인 구직 횟수 조회")
    @GetMapping("/{id}/RRCount")
    @ResponseStatus(OK)
    public MemberRRCountResponse getMemberRRCount(final @PathVariable("id") @Positive Long memberId) {
        return memberService.getMemberRRCount(memberId);
    }

    @Operation(summary = "비밀번호 찾기 - 멤버 비밀번호 수정")
    @PatchMapping("/{id}/passwordUpdate")
    @ResponseStatus(NO_CONTENT)
    public void updateMemberPasswd(final @PathVariable("id") @Positive Long memberId, final @RequestBody @Validated MemberPasswdUpdateRequest request) {
        memberService.updatePasswd(memberId, request.password());
    }

    @Operation(summary = "마이페이지 - 멤버 비밀번호 수정")
    @PatchMapping("/mypage/{id}/password-update")
    @ResponseStatus(NO_CONTENT)
    public void mypageUpdateMemberPasswd(final @PathVariable("id") @Positive Long memberId, final @RequestBody @Validated MemberPasswdUpdateRequest request) {
        memberService.updatePasswd(memberId, request.password());
    }

    @Operation(summary = "멤버 탈퇴")
    @PatchMapping("/{id}/draw")
    @ResponseStatus(NO_CONTENT)
    public void withDraw(final @PathVariable("id") @Positive Long memberId) {
        memberService.draw(memberId);
    }

    @Operation(summary = "멤버 정지")
    @PatchMapping("/{id}/ban")
    @ResponseStatus(NO_CONTENT)
    public void banMember(final @PathVariable("id") @Positive Long memberId) {
        memberService.ban(memberId);
    }

    @Operation(summary = "멤버 복구")
    @PatchMapping("{id}/general")
    @ResponseStatus(NO_CONTENT)
    public void generalMember(final @PathVariable("id") @Positive Long memberId) {
        memberService.general(memberId);
    }

    @Operation(summary = "멤버 검색")
    @GetMapping("/search")
    @ResponseStatus(OK)
    public Page<MemberListResponse> search(
            @RequestParam(required = false) final String name,
            @RequestParam(required = false) final String nickname,
            @RequestParam(required = false) @Email final String email,
            @RequestParam(required = false) final Integer reportedCnt,
            Pageable pageable
    ) {
        return memberService.findBySearchCond(name, nickname, email, reportedCnt, pageable);
    }

    @Operation(summary = "닉네임 중복 확인")
    @GetMapping("/check-nickname")
    @ResponseStatus(OK)
    public NicknameCheckResponse checkNickname(final @RequestParam @NotBlank String nickname) {
        return memberService.NicknameCheck(nickname);
    }

    @Operation(summary = "이메일 중복 확인")
    @GetMapping("/check-email")
    @ResponseStatus(OK)
    public EmailCheckResponse checkEmail(final @RequestParam @Email String email) {
        return memberService.EmailCheck(email);
    }

    @Operation(summary = "이메일 찾기")
    @PostMapping("/find-email")
    @ResponseStatus(OK)
    public FindEmailResponse findEmail(final @RequestBody @Validated FindEmailRequest request) {
        return memberService.findEmail(request.name(), request.phoneNumber());
    }

    @Operation(summary = "비밀번호 찾기")
    @PostMapping("/find-passwd")
    @ResponseStatus(OK)
    public FindPasswdResponse findPasswd(final @RequestBody @Validated FindPasswdRequest request) {
        return memberService.findPasswd(request.name(), request.email());
    }

    @Operation(summary = "프로파일")
    @GetMapping("/{nickname}/dashboard")
    @ResponseStatus(OK)
    public ProfileResponse profile(final @PathVariable("nickname") @NotBlank String nickname) {
        return memberService.findProfile(nickname);
    }

    @Operation(summary = "마이페이지 - 주소 선택? 동네 설정?")
    @PatchMapping("/{id}/selected-addresses")
    @ResponseStatus(NO_CONTENT)
    public void updateSelectedAddresses(final @PathVariable("id") @Positive Long memberId, final @RequestBody @Validated UpdateSelectedAddrRequest request) {
        memberService.updateSeletedAddr(memberId, request.selectedAddresses());
    }

    @Operation(summary = "마이페이지 - 내 주소, 선택 주소 요청")
    @GetMapping("/{id}/addresses")
    @ResponseStatus(OK)
    public AddressesGetResponse getAddresses(final @PathVariable("id") @Positive Long memberId) {
        return memberService.getAddressesByMemberId(memberId);
    }

    @Operation(summary = "마이페이지 - 좌측 사이드바 사용자 데이터 요청")
    @GetMapping("/{id}/sidebar-info")
    @ResponseStatus(OK)
    public SideBarInfoResponse getSideBarInfo(final @PathVariable("id") @Positive Long memberId) {
        return memberService.getSidebarinfoBy(memberId);
    }
}
