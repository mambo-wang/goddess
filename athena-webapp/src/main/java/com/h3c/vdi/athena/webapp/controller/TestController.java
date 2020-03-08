package com.h3c.vdi.athena.webapp.controller;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by w16051 on 2018/3/7.
 */
@Api(value = "测试用Controller", tags = {"测试用Controller"})
@Controller
public class TestController {

    @Deprecated
    @GetMapping(value = "/ws")
    public String ws(){
        return "ws";
    }
}
