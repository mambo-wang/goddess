package com.h3c.vdi.athena.gateway.controller;

import com.h3c.vdi.athena.gateway.feign.UserService;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by JemmyZhang on 2018/2/24
 */
@RestController
@RequestMapping("/token")
public class TokenController {

    @Resource
    UserService userService;

    @GetMapping
    public String tokenGet(){
        return "Login system to get an access token!";
    }

    @PostMapping
    public String tokenPost(){
        return "Successfully authenticated!";
    }

    @GetMapping("/test")
    public void exceptionTester(){
        try{
            userService.error();
        } catch (HystrixRuntimeException e){
            throw e;
        }
    }

    @GetMapping("/auth_error")
    public String authError(){
        try {
            return userService.authError();
        }catch (HystrixRuntimeException e){
            throw e;
        }

    }
}
