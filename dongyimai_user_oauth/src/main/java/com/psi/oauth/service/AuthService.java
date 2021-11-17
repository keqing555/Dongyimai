package com.psi.oauth.service;

import com.psi.oauth.util.AuthToken;

public interface AuthService {
    /**
     * 登录授权认证
     *
     * @param username
     * @param password
     * @param clientId
     * @param clientSecret
     * @return
     */
    AuthToken login(String username, String password, String clientId, String clientSecret);
}
