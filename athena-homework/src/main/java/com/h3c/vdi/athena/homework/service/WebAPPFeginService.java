package com.h3c.vdi.athena.homework.service;

import com.h3c.vdi.athena.homework.dto.UserDTO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by w16051 on 2018/3/6.
 */
@FeignClient(name = "athena-webapp")
public interface WebAPPFeginService {

    @RequestMapping("/welcome")
    @SendTo("/topic/getResponse")
    UserDTO say(@RequestBody UserDTO userDTO);

}
