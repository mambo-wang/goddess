package com.h3c.vdi.athena.keystone.controller;

import com.h3c.vdi.athena.common.exception.AppException;
import com.h3c.vdi.athena.keystone.exception.ErrorCodes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by JemmyZhang on 2018/2/13
 */
@RestController
public class HomeController {

    @GetMapping({"/","/home","/index"})
    public String home() {
        return "Base home!";
    }

    @RequestMapping("/errors")
    public String error(){
        throw new AppException(ErrorCodes.DEFAULT_ERROR_NUMBER);
    }

    @RequestMapping("/auth_error")
    public String authError(){
        return "Hello World";
    }
}
