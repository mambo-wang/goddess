package com.h3c.vdi.athena.gateway;

import com.h3c.vdi.athena.gateway.config.filter.PreRequestLogZuulFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by JemmyZhang on 2018/2/24
 * @author w14014
 */
@SpringBootApplication
@EnableFeignClients
@EnableZuulProxy //默认整合了Hystrix
@EnableCircuitBreaker
@ComponentScan(basePackages = {"com.h3c.vdi.athena.gateway", "com.h3c.vdi.athena.common"})
public class GatewayApplication{

    @Bean
    public PreRequestLogZuulFilter preRequestLogFilter() {
        return new PreRequestLogZuulFilter();
    }

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class);
    }
}
