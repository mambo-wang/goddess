package com.h3c.vdi.athena.webapp;

import com.h3c.vdi.athena.webapp.config.annotation.ExcludeScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author w14014
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@EnableCircuitBreaker
@EnableSwagger2
@ComponentScan(basePackages = {"com.h3c.vdi.athena.webapp"}, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = {ExcludeScan.class})
})
public class WebAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebAppApplication.class);
    }
}
