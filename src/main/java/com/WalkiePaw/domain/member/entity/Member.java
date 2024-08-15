package com.WalkiePaw.domain.member.entity;

import static com.WalkiePaw.domain.member.entity.Role.USER;

import com.WalkiePaw.domain.common.BaseEntity;
import com.WalkiePaw.presentation.domain.member.dto.MemberUpdateParam;
import com.WalkiePaw.presentation.domain.member.dto.SocialSignUpParam;
import jakarta.persistence.*;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

import java.time.LocalDate;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;
    @Column(name = "member_name")
    private String name;
    private String nickname;
    private String email;
    @JsonIgnore
    private String password;
    private String phoneNumber;
    @Column(name = "member_address")
    private String memberAddress;
    @Column(name = "selected_addresses")
    private String selectedAddresses;
    private LocalDate birth;
    private String profile;
    private double rating;
    @Column(name = "member_photo")
    private String photo;
    @Enumerated(EnumType.STRING)
    private MemberStatus status;
    private int reportedCnt;
    private int recruitCnt;
    private int researchCnt;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Enumerated(EnumType.STRING)
    private SocialType socialType; // KAKAO, NAVER, GOOGLE
    private String socialId; // 로그인한 소셜 타입의 식별자 값 (일반 로그인인 경우 null)

    @Builder
    public Member(String name, String nickname, String email, String password, String phoneNumber,
                  String memberAddress, LocalDate birth, String profile, double rating,
                  String photo, SocialType socialType, String socialId) {
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.memberAddress = memberAddress;
        this.birth = birth;
        this.profile = profile;
        this.rating = rating;
        this.photo = photo;
        this.status = MemberStatus.GENERAL;
        this.role = USER;
        this.socialType = socialType;
        this.socialId = socialId;
    }

    public void updateMember(MemberUpdateParam param) {
        this.name = param.getName();
        this.nickname = param.getNickname();
        this.phoneNumber = param.getPhoneNumber();
        this.memberAddress = param.getAddress();
        this.birth = param.getBirth();
        this.profile = param.getProfile();
        this.rating = param.getRating();
        this.photo = param.getPhoto();
        this.status = param.getStatus();
        this.reportedCnt = param.getReportedCnt();
    }

    public void updatePasswd(String password) {
        this.password = password;
    }

    //    /**
//     * Member 생성 메서드
//     */
//    public static Member createMember(String name, String email, String password, String phoneNumber,
//                               String address, String gender, LocalDate birth, String profile, String photo) {
//        return new Member(name, email, password, phoneNumber,
//                address, gender, birth, profile, photo);
//    }

    /**
     * 회원 탈퇴 로직
     */
    public void withdraw() {
        status = MemberStatus.WITHDRAWN;
    }

    public void ban() {
        status = MemberStatus.BANNED;
    }

    public void general() {
        status = MemberStatus.GENERAL;
    }

    public void setRoleToGuest() {
        role = Role.GUEST;
    }

    // 유저 권한 설정 메소드
    public void authorizeUser() {
        this.role = USER;
    }

    // 비밀번호 암호화 메소드
    public void passwordEncode(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }

    public Long updateBySocialSignUpRequest(final SocialSignUpParam param) {
        this.nickname = param.getNickname();
        this.phoneNumber = param.getPhoneNumber();
        this.birth = param.getBirth();
        this.memberAddress = param.getAddress();
        this.profile = param.getProfile();
        this.photo = param.getPhoto();
        this.role = USER;
        return this.id;
    }

    public void updateSelectedAdrrs(String selectedAddresses) {
        this.selectedAddresses = selectedAddresses;
    }

    /**
     * TODO
     *  - 비밀번호 암호화 메서드 구현 => 서비스단에서 암호화 처리함.
     *  - 회원 정지 로직 추가 => 완료
     *  - update 메서드 추가
     *  - validation 추가
     */
}
