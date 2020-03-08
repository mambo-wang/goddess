package com.h3c.vdi.athena.gateway.feign;

import com.h3c.vdi.athena.gateway.dto.UserDTO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * TODO 缺少接口
 * Created by JemmyZhang on 2018/2/24
 */

@FeignClient(name = "athena-homework")
public interface HomeworkService {

    @RequestMapping(value = "/users/userName/{userName}",method = RequestMethod.GET)
    UserDTO findUserDTOByLoginName(@PathVariable("userName") String loginName);

}
