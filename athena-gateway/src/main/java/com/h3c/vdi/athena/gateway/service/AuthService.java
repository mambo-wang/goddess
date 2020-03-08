package com.h3c.vdi.athena.gateway.service;

import com.h3c.vdi.athena.gateway.dto.LoginRespDTO;

/**
 * Created by w14014 on 2018/3/2.
 */
public interface AuthService {

    /**
     * 登陆时生成token，完成Spring Security认证，然后返回给客户端
     * @param username 用户名
     * @param password 密码
     * @return LoginRespDTO
     */
    LoginRespDTO login(String username, String password);

    /**
     * 刷新token的接口用来获取新的token
     * @param oldToken
     * @return
     */
    String refresh(String oldToken);

    void invalidate(String token);
}
