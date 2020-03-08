package com.h3c.vdi.athena.webapp.config.feign;

import com.h3c.vdi.athena.webapp.config.annotation.ExcludeScan;
import com.h3c.vdi.athena.webapp.config.encoder.FeignSpringFormEncoder;
import feign.Feign;
import feign.Logger;
import feign.Request;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.cloud.netflix.feign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;


/**
 *
 * @author w16051
 * @date 2018/4/26
 */
@ExcludeScan
@Configuration
public class FeignMultipartSupportConfig {

    @Autowired
    private ObjectFactory<HttpMessageConverters> messageConverters;

    @Bean
    @Primary
    @Scope("prototype")
    public Encoder multipartFormEncoder(){
        return new SpringFormEncoder(new SpringEncoder(messageConverters));
    }

    @Bean
    public feign.Logger.Level multipartLoggerLevel(){
        return Logger.Level.FULL;
    }

    @Bean
    public Request.Options feignOptions(){
        return new Request.Options(60000, 120000);
    }

    /**
     * Feign默认支持的是HystrixFeign.Builder，通过配置后，返回的是Feign.builder去掉了对Hystrix的支持。
     * 从而实现对使用本配置类的FeignClient禁用Hystrix
     * @return
     */
    @Bean
    @Primary
    @Scope("prototype")
    public Feign.Builder feignBuilder(){
        return Feign.builder();
    }
}
