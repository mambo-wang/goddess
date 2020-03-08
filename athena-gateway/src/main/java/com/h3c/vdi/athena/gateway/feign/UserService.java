package com.h3c.vdi.athena.gateway.feign;

import com.h3c.vdi.athena.gateway.config.feign.FeignConfiguration;
import com.h3c.vdi.athena.gateway.dto.SecureUserDTO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by JemmyZhang on 2018/2/24
 */

@FeignClient(name = "athena-keystone", configuration = FeignConfiguration.class)
public interface UserService {

    @RequestMapping(value = "/users/secure",method = RequestMethod.GET)
    SecureUserDTO findUserByLoginName(@RequestParam("login_name") String loginName);

    @RequestMapping(value = "/errors")
    String error();

    @RequestMapping(value = "/auth_error")
    String authError();
}
