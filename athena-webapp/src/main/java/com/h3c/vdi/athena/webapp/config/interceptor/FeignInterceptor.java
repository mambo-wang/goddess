package com.h3c.vdi.athena.webapp.config.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Objects;

/**
 * Created by w14014 on 2018/3/3.
 *
 * feign转发请求会丢失头部信息，在这里加上
 */
@Component
public class FeignInterceptor implements RequestInterceptor {

    @Resource
    HttpServletRequest request;

    @Override
    public void apply(RequestTemplate requestTemplate) {

        Enumeration<String> headerNames = request.getHeaderNames();

        if(Objects.nonNull(headerNames)){
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                String value = request.getHeader(name);
                requestTemplate.header(name, value);
            }
        }
       //requestTemplate.header("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJ1c2VybmFtZVwiOlwic3R1XCIsXCJwYXNzd29yZFwiOlwid2FuZ2Jhb1wiLFwiYXV0aG9yaXR5VmFsdWVzXCI6W1wiU1lTVEVNXCIsXCJBRE1JTlwiXX0iLCJleHAiOjI1MzM3MDczNjAwMH0.MucYOjy-Zm-iPSL0FCoTr4NqPHm_n3Lr3d6nutEbWfAnUfuE1PXmnG4mFLlEaTDabCwHSgzI7DknjtJWI4I9qA");
    }
}
