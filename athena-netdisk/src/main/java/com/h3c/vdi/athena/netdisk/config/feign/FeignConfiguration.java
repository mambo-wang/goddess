package com.h3c.vdi.athena.netdisk.config.feign;

import feign.Logger;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by w14014 on 2018/2/11.
 */
@Configuration
public class FeignConfiguration {

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.BASIC;
    }

    /**
     * 配置哪些错误可以不走熔断机制
     * @return
     */
    @Bean
    public ErrorDecoder errorDecoder(){
        return new UserErrorDecoder();
    }
}
