package com.h3c.vdi.athena.homework.controller;

import com.h3c.vdi.athena.common.exception.AppException;
import com.h3c.vdi.athena.homework.dto.UserDTO;

//import com.h3c.vdi.athena.homework.exception.ErrorCodes;
import com.h3c.vdi.athena.homework.service.TestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by JemmyZhang on 2018/2/13
 */
@RestController
public class HomeController {

    @Resource
    TestService testService;

    @GetMapping("/homework")
    public List<UserDTO> find(){
        return testService.findUsers();
    }

    @GetMapping("/test")
    public String test(){
        throw new AppException("测试409自定义异常");
    }

//    @GetMapping("/errors")
//    public String error(){
//        return ErrorCodes.getErrorMessage(ErrorCodes.HTTP_INTERNAL_SERVER_ERROR);
//    }
}
