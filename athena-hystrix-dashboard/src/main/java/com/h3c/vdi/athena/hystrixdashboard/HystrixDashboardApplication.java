package com.h3c.vdi.athena.hystrixdashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.turbine.EnableTurbine;
import zipkin.server.EnableZipkinServer;

/**
 * Created by w14014 on 2018/3/26.
 */
@SpringBootApplication
@EnableHystrixDashboard
@EnableCircuitBreaker
@EnableTurbine
@EnableZipkinServer
public class HystrixDashboardApplication{

    public static void main(String[] args) {
        SpringApplication.run(HystrixDashboardApplication.class, args);
    }
}
