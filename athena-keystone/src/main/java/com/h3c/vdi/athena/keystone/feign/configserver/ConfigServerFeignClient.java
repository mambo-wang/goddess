package com.h3c.vdi.athena.keystone.feign.configserver;

import com.h3c.vdi.athena.keystone.config.feign.FeignConfiguration;
import com.h3c.vdi.athena.keystone.dto.ConfigDTO;
import com.h3c.vdi.athena.keystone.feign.model.CreateUserReq;
import com.h3c.vdi.athena.keystone.feign.model.ModifyUserReq;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author w14014
 * @date 2018/9/26
 */
@FeignClient(name = "athena-config", configuration = FeignConfiguration.class)
public interface ConfigServerFeignClient {

    @RequestMapping(value = "/config/quota", method = RequestMethod.GET)
    ConfigDTO queryQuota();

    /**
     * 删除某个用户的皮肤设置，删除用户或者归档时使用
     * @param username 用户名
     */
    @DeleteMapping(value = "/config/skin/{username}")
    void deleteSkinConfig(@PathVariable(value = "username")String username);
}


