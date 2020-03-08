package com.h3c.vdi.athena.gateway.controller;

import com.h3c.vdi.athena.common.exception.AppException;
import com.h3c.vdi.athena.common.model.CommonDTO;
import com.h3c.vdi.athena.common.utils.JwtTokenUtilBean;
import com.h3c.vdi.athena.gateway.config.security.JwtAuthenticationRequest;
import com.h3c.vdi.athena.gateway.dto.LoginRespDTO;
import com.h3c.vdi.athena.gateway.dto.UserDTO;
import com.h3c.vdi.athena.gateway.service.AuthService;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by JemmyZhang on 2018/2/24
 */
@RestController
public class HomeController {

    @Resource
    private AuthService authService;

    @PostMapping(value = "/auth")
    public LoginRespDTO login(@RequestBody JwtAuthenticationRequest loginInfo) throws AuthenticationException {
        LoginRespDTO loginRespDTO = authService.login(loginInfo.getUsername(), loginInfo.getPassword());
        return loginRespDTO;
    }

    @DeleteMapping(value = "/invalidate_token")
    public CommonDTO<String> createAuthenticationToken(HttpServletResponse response) {
        response.setHeader(JwtTokenUtilBean.TOKEN_HEADER, null);
        return new CommonDTO<>(CommonDTO.Result.SUCCESS, "success", "");
    }

    @GetMapping(value = "/refresh_token")
    public CommonDTO<String> refreshAndGetAuthenticationToken(HttpServletRequest request) throws AuthenticationException {
        String token = request.getHeader(JwtTokenUtilBean.TOKEN_HEADER);
        String refreshedToken = authService.refresh(token);
        if (refreshedToken == null) {
            throw new AppException("token失效，请重新登陆");
        } else {
            return new CommonDTO<>(CommonDTO.Result.SUCCESS, "seccess", refreshedToken);
        }
    }
}
