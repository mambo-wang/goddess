package com.h3c.vdi.athena.configserver.config.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by JemmyZhang on 2018/2/26
 */

@Configuration
@PropertySource("classpath:portus")
public class FeignTokenInterceptor implements RequestInterceptor {

    @Value("${value}")
    String value;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header("Authorization", value);
    }
}
