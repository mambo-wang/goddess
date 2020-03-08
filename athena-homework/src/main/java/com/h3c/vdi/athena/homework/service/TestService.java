package com.h3c.vdi.athena.homework.service;

import com.h3c.vdi.athena.homework.dto.UserDTO;
import com.h3c.vdi.athena.homework.feign.keystone.UserFeignService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by JemmyZhang on 2018/2/26
 */
@Component
public class TestService {

    @Resource
    UserFeignService userFeignService;

    public List<UserDTO> findUsers(){
        return userFeignService.finds(null,null,null);
    }
}
