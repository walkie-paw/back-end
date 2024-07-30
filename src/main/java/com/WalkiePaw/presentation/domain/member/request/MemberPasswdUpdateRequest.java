package com.WalkiePaw.presentation.domain.member.request;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class MemberPasswdUpdateRequest {
    private String password;
}
