package com.h3c.vdi.athena.keystone.feign.homework;

import com.h3c.vdi.athena.keystone.config.feign.FeignConfiguration;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by w16051 on 2018/10/30.
 */
@FeignClient(name = "athena-homework", configuration = FeignConfiguration.class)
public interface HomeworkFeignService {

    @RequestMapping(value = "/users/delete/{userId}/role/{roleId}",method = RequestMethod.PUT)
    void deleteUser(@PathVariable(value = "userId") Long userId,@PathVariable(value = "roleId") Long roleId);

}
