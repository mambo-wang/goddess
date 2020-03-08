package com.h3c.vdi.athena.configserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author w14014
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@ComponentScan(basePackages = {"com.h3c.vdi.athena.configserver", "com.h3c.vdi.athena.common"})
public class ConfigServerApplication{

    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class, args);
    }
}
