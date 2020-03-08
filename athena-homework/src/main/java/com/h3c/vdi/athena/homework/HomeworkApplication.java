
package com.h3c.vdi.athena.homework;

import com.h3c.vdi.athena.homework.service.attachment.CustomMultipartResolver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.MultipartAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.multipart.MultipartResolver;

/**
 * Created by JemmyZhang on 2018/2/13
 * @author w14014
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
//@EnableAutoConfiguration(exclude = {MultipartAutoConfiguration.class})
@ServletComponentScan(basePackages = {"com.h3c.vdi.athena.homework"})
@ComponentScan(basePackages = {"com.h3c.vdi.athena.homework", "com.h3c.vdi.athena.common"})
public class HomeworkApplication {

//    @Bean(name = "multipartResolver")
//    public MultipartResolver multipartResolver(){
//        MultipartResolver customMultiPartResolver = new CustomMultipartResolver();
//        return customMultiPartResolver;
//    }

    public static void main(String[] args) {
        SpringApplication.run(HomeworkApplication.class);
    }
}
