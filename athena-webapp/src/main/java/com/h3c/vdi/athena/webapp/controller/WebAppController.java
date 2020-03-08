package com.h3c.vdi.athena.webapp.controller;

import com.h3c.vdi.athena.common.model.CommonDTO;
import com.h3c.vdi.athena.webapp.service.AuthFeignService;
import com.h3c.vdi.athena.webapp.service.HomeworkFeignService;
import com.h3c.vdi.athena.webapp.service.KeystoneFeignService;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by w14014 on 2018/3/2.
 */
@Deprecated
@Api(value = "webapp测试Controller", tags = {"test"})
@RestController
@RequestMapping("/webapp")
@RefreshScope //添加该注解的类会在配置更改时得到特殊的处理
public class WebAppController {

    private Logger logger = LoggerFactory.getLogger(WebAppController.class);

    @Resource
    private HomeworkFeignService homeworkFeignService;

    @Resource
    private KeystoneFeignService keystoneFeignService;

    @Resource
    private AuthFeignService authFeignService;

    /** 绑定Git仓库配置文件中的profile属性 */
//    @Value("${profile}")
    private String profile = "xxx";

    @GetMapping("/profile")
    public String hello() {
        return this.profile;
    }

    @GetMapping(value = "/test_error")
    public String test() throws Throwable {

//        try {
//            String test = homeworkService.test();
//            return new CommonDTO<>(CommonDTO.Result.SUCCESS, "成功", test);
//        } catch (Exception e) {
//            e.printStackTrace();
//            Throwable throwable = e.getCause();
//            //TODO 获取不到errorcode
//            return new CommonDTO<>(CommonDTO.Result.FAILURE, throwable.getMessage(), 403);
//        }
        try{
            return authFeignService.expTest();
        }catch (HystrixRuntimeException e){
            throw e;
        }catch (Exception e){
            throw e;
        }
    }

    @GetMapping(value = "/auth_error")
    public String authError(){
        try{
            return authFeignService.authError();
        }catch (HystrixRuntimeException e){
            throw e;
        }catch (Exception e){
            throw e;
        }
    }

    @GetMapping(value = "/hi")
    public CommonDTO<String> hi(@RequestParam(value = "login_name")String loginName){

//        try {
            String hi = keystoneFeignService.hi(loginName);
            return new CommonDTO<String>(CommonDTO.Result.SUCCESS, "", hi);
//        } catch (Exception e) {
//            return new CommonDTO<>(CommonDTO.Result.FAILURE, e.getMessage());
//        }
    }

}
