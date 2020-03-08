package com.h3c.vdi.athena.keystone.feign.netdisk;

import com.h3c.vdi.athena.keystone.config.feign.FeignConfiguration;
import com.h3c.vdi.athena.keystone.feign.model.CreateUserReq;
import com.h3c.vdi.athena.keystone.feign.model.ModifyUserReq;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author w14014
 * @date 2018/9/26
 */
@FeignClient(name = "athena-netdisk", configuration = FeignConfiguration.class)
public interface NetDiskFeignClient {

    @RequestMapping(value = "/ncuser/users", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void createUser(@RequestBody CreateUserReq createUserReq);

    @RequestMapping(value = "/ncuser/users/{userId}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    void modifyUser(@PathVariable(name = "userId") String userId, @RequestBody ModifyUserReq modifyUserReq);

    @RequestMapping(value = "/ncuser/users/{userId}", method = RequestMethod.DELETE)
    void removeUser(@PathVariable(name = "userId") String userId);

    @RequestMapping(value = "/ncuser/users/batch/{userIds}", method = RequestMethod.DELETE)
    void removeUser(@PathVariable(name = "userIds") String[] userIds);
}


