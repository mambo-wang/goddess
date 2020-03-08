package com.h3c.vdi.athena.webapp.controller;

import com.h3c.vdi.athena.common.model.CommonDTO;
import com.h3c.vdi.athena.webapp.dto.JwtAuthenticationRequest;
import com.h3c.vdi.athena.webapp.dto.LoginRespDTO;
import com.h3c.vdi.athena.webapp.dto.UserDTO;
import com.h3c.vdi.athena.webapp.service.AuthFeignService;
import com.h3c.vdi.athena.webapp.service.KeystoneFeignService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

@Api(value = "登陆认证Controller", tags = {"登陆、注销、刷新token、查询当前登录用户"})
@RestController
public class AuthController {

    @Resource
    private AuthFeignService authFeignService;

    @Resource
    private KeystoneFeignService keystoneFeignService;

    @ApiOperation(value = "登陆",notes = "根据用户名密码进行登陆操作")
    @ApiImplicitParam(name = "JwtAuthenticationRequest", value = "用户名密码",dataType = "JwtAuthenticationRequest", required = true)
    @PostMapping(value = "/login")
    public LoginRespDTO login(@RequestBody JwtAuthenticationRequest request){
        LoginRespDTO dto = authFeignService.login(request);
        return dto;
    }

    @GetMapping(value = "/refresh_token")
    public CommonDTO<String> refresh() {
        return authFeignService.refreshToken();
    }

    @DeleteMapping(value = "/logout")
    public CommonDTO<String> invalidateToken(){
        return authFeignService.invalidateToken();
    }

    @GetMapping(value = "/current_login_user")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header")
    public CommonDTO<UserDTO> queryLoginUser(){
        UserDTO userDTO = keystoneFeignService.currentLoginUser();
        return new CommonDTO<>(CommonDTO.Result.SUCCESS, "", userDTO);
    }
}
