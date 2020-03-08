package com.h3c.vdi.athena.gateway.service;

import com.h3c.vdi.athena.common.exception.AppException;
import com.h3c.vdi.athena.common.utils.EncryptUtils;
import com.h3c.vdi.athena.common.utils.JwtTokenUtilBean;
import com.h3c.vdi.athena.common.utils.MD5Util;
import com.h3c.vdi.athena.common.utils.RedisUtil;
import com.h3c.vdi.athena.gateway.config.security.JwtUserDetailsServiceImpl;
import com.h3c.vdi.athena.gateway.dto.LoginRespDTO;
import com.h3c.vdi.athena.gateway.dto.UserDTO;
import com.h3c.vdi.athena.gateway.exception.ErrorCodes;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by w14014 on 2018/3/2.
 */
@Service("/authService")
@PropertySource("classpath:conf/config.properties")
public class AuthServiceImpl implements AuthService{

    @Resource
    private JwtUserDetailsServiceImpl userDetailsService;

    @Resource
    private JwtTokenUtilBean jwtTokenUtilBean;

    @Resource
    private RedisUtil redisUtil;

    @Value("${token.expire-time-in-minutes}")
    private int tokenOutDateTime;

    @Override
    public LoginRespDTO login(String username, String password){
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, password);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if(MD5Util.authenticatePassword(userDetails.getPassword(), password)){
            SecurityContextHolder.getContext().setAuthentication(upToken);
            //TODO 前端往后端传也要加密的
            boolean result = redisUtil.set(username, password);
            redisUtil.expire(username, tokenOutDateTime * 60);
            String token = jwtTokenUtilBean.generateToken(userDetails);
            UserDTO userDTO = userDetailsService.findUserDTOByUsername(username);
            LoginRespDTO loginRespDTO = new LoginRespDTO(token, userDTO);
            return loginRespDTO;
        }
        throw new AppException(ErrorCodes.LOGIN_ERROR);
    }

    @Override
    public String refresh(String oldToken) {
        String newToken = jwtTokenUtilBean.refreshToken(oldToken);
        return newToken;
    }

    /**
     * 使token失效，退出登陆时是不是只要浏览器把token丢弃掉就可以啦
     * @param token
     */
    @Override
    public void invalidate(String token) {

    }
}
