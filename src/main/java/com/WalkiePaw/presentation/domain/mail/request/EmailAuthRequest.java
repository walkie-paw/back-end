package com.WalkiePaw.presentation.domain.mail.request;

import lombok.Getter;

@Getter
public class EmailAuthRequest {

    private String email;
    private String authNum;
}
