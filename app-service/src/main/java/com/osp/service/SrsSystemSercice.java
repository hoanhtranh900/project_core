package com.osp.service;

import com.osp.core.dto.request.LoginRequest;
import com.osp.core.dto.response.UserInfo;
import com.osp.core.dto.wso2is.ISTokenInfo;
import com.osp.core.exception.UnauthorizedException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/*
* Service dành cho logic hệ thống
* */
public interface SrsSystemSercice {
    //Login đăng nhập JWT
    UserInfo loginJWT(LoginRequest loginRequest) throws UnauthorizedException;
    //Login đăng nhập JWT By refreshToken
    UserInfo getAccessTokenByRefreshTokenJwt(String refreshToken) throws UnauthorizedException;
    //Logout JWT
    boolean logoutJwt();
    //Login đăng nhập wso2is
    UserInfo loginIS(String authorizationCode) throws UnauthorizedException;
    //RefreshToken wso2is
    ISTokenInfo getAccessTokenByRefreshToken(String refreshToken);
}
