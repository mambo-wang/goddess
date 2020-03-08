package com.h3c.vdi.athena.gateway.config.web;

import com.h3c.vdi.athena.common.decoder.UserErrorDecoder;
import com.h3c.vdi.athena.common.exception.ExceptionAdvisor;
import com.h3c.vdi.athena.common.utils.JwtTokenUtilBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by JemmyZhang on 2018/3/7
 */

@Configuration
@PropertySource("classpath:conf/config.properties")
public class WebApplicationConfig extends WebMvcConfigurerAdapter{

    @Value("${token.expire-time-in-minutes}")
    private int tokenOutDateTime;

    @Bean
    public UserErrorDecoder userErrorDecoder(){
        return new UserErrorDecoder();
    }

    @Bean
    public ExceptionAdvisor exceptionAdvisor(){
        return new ExceptionAdvisor();
    }

    @Bean
    public JwtTokenUtilBean jwtTokenUtil(){
        JwtTokenUtilBean jwtTokenUtilBean = new JwtTokenUtilBean();
        jwtTokenUtilBean.setTimeoutMinutes(tokenOutDateTime);
        return jwtTokenUtilBean;
    }
}
