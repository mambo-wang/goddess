package com.h3c.vdi.athena.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Created by JemmyZhang on 2018/2/13
 */

@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication{
    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class);
    }
}
