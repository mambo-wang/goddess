package com.h3c.vdi.athena.netdisk.config.feign;

import com.google.gson.Gson;
import com.h3c.vdi.athena.common.exception.AppException;
import com.h3c.vdi.athena.common.model.DefaultExceptionInfo;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

/**
 * Created by w14014 on 2018/2/11.
 */
@Configuration
public class UserErrorDecoder implements ErrorDecoder {

    private Logger logger = LoggerFactory.getLogger(UserErrorDecoder.class);

    @Override
    public Exception decode(String methodKey, Response response) {

            try {
                if(response.body() != null) {
                    String body = Util.toString(response.body().asReader());
                    logger.error("feign catch exception info is " + body);
                    DefaultExceptionInfo info = new Gson().fromJson(body, DefaultExceptionInfo.class);
                    return new AppException(info.getStatus(), info.getMessage());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        return new AppException(500, "服务调用异常");
    }
}
