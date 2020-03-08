package com.h3c.vdi.athena.keystone.config.web;

import com.h3c.vdi.athena.common.decoder.UserErrorDecoder;
import com.h3c.vdi.athena.keystone.exception.ExceptionAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by JemmyZhang on 2018/3/7
 */

@Configuration
public class WebApplicationConfig{

    @Bean
    public UserErrorDecoder userErrorDecoder(){
        return new UserErrorDecoder();
    }

    @Bean
    public ExceptionAdvisor exceptionAdvisor(){
        return new ExceptionAdvisor();
    }
}
