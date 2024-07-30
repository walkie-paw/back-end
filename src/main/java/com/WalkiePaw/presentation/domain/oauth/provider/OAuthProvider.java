package com.WalkiePaw.presentation.domain.oauth.provider;

import com.WalkiePaw.presentation.domain.oauth.response.OAuthUserinfoResponse;

public interface OAuthProvider {
    OAuthUserinfoResponse getUserInfo(final String code);
}
