package com.WalkiePaw.presentation.domain.oauth;

import lombok.Data;

@Data
public class OAuthUserinfoResponse {
    private final String email;
    private final String name;
}
