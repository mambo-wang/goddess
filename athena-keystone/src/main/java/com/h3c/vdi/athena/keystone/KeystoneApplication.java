package com.h3c.vdi.athena.keystone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

/**
 * Created by JemmyZhang on 2018/2/13
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class KeystoneApplication {


    public static void main(String[] args) {
        SpringApplication.run(KeystoneApplication.class);
    }
}
