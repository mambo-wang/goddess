package com.h3c.vdi.athena.netdisk;

import com.h3c.vdi.athena.netdisk.upload.CustomMultipartResolver;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.MultipartAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;

/**
 *
 * @author w14014
 * @date 2018/9/17
 */
@EnableAutoConfiguration(exclude = {MultipartAutoConfiguration.class})
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@Configuration
@ComponentScan(basePackages = {"com.h3c.vdi.athena.netdisk", "com.h3c.vdi.athena.common"})
@ServletComponentScan(basePackages = {"com.h3c.vdi.athena.netdisk", "com.h3c.vdi.athena.common"})
@MapperScan("com.h3c.vdi.athena.netdisk.mappers")
public class NetdiskApplication {

    @Bean(name = "multipartResolver")
    public MultipartResolver multipartResolver(){
        MultipartResolver customMultiPartResolver = new CustomMultipartResolver();
        return customMultiPartResolver;
    }


    public static void main(String[] args) {
        SpringApplication.run(NetdiskApplication.class);
    }
}
