package com.h3c.vdi.athena.netdisk.feign.homework;

import com.h3c.vdi.athena.netdisk.config.feign.FeignConfiguration;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author w14014
 * @date 2018/10/31
 */
@FeignClient(name = "athena-homework", configuration = FeignConfiguration.class)
public interface HomeworkFeignClient {

    @RequestMapping(value = "/groups/username/{username}",method = RequestMethod.GET)
    String queryGroupsByUsername(@PathVariable(name = "username")String username);
}
