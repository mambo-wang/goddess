package com.h3c.vdi.athena.webapp.service;

import com.h3c.vdi.athena.common.model.CommonDTO;
import com.h3c.vdi.athena.webapp.config.feign.FeignMultipartSupportConfig;
import com.h3c.vdi.athena.webapp.dto.JwtAuthenticationRequest;
import com.h3c.vdi.athena.webapp.dto.LoginRespDTO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author w14014
 * @date 2018/3/2
 */
@FeignClient(value = "athena-gateway", path = "/", configuration = FeignMultipartSupportConfig.class)
public interface AuthFeignService {

    /**
     * 用户登陆请求
     */
    @RequestMapping(value = "/auth", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    LoginRespDTO login(@RequestBody JwtAuthenticationRequest request);

    @RequestMapping(value = "/invalidate_token", method = RequestMethod.DELETE)
    CommonDTO<String> invalidateToken();

    @RequestMapping(value = "/refresh_token", method = RequestMethod.GET)
    CommonDTO<String> refreshToken();

    @RequestMapping(value = "/token", method = RequestMethod.GET)
    String expTest();

    @RequestMapping(value = "/token/auth_error")
    String authError();
}
