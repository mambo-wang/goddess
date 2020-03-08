package com.h3c.vdi.athena.netdisk.config.web;

import com.h3c.vdi.athena.common.decoder.UserErrorDecoder;
import com.h3c.vdi.athena.common.exception.ExceptionAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by JemmyZhang on 2018/3/7
 */

@Configuration
public class WebApplicationConfig extends WebMvcConfigurerAdapter{

    @Bean
    public UserErrorDecoder userErrorDecoder(){
        return new UserErrorDecoder();
    }

    @Bean
    public ExceptionAdvisor exceptionAdvisor(){
        return new ExceptionAdvisor();
    }
}
