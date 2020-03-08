package com.h3c.vdi.athena.configserver.feign;

import com.h3c.vdi.athena.configserver.config.feign.FeignConfiguration;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author w14014
 * @date 2018/9/26
 */
@FeignClient(name = "athena-netdisk", configuration = FeignConfiguration.class)
public interface NetDiskFeignClient {

    @RequestMapping(value = "/ncuser/users/quota/{quota}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    void modifyQuota(@PathVariable(name = "quota") String quota);

    @RequestMapping(value = "/netdisk/init", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    void init();


}


