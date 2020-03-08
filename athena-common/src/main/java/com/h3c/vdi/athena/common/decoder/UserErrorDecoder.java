package com.h3c.vdi.athena.common.decoder;

import com.google.gson.Gson;
import com.h3c.vdi.athena.common.exception.AppException;
import com.h3c.vdi.athena.common.exception.BasicHttpException;
import com.h3c.vdi.athena.common.model.DefaultExceptionInfo;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import io.jsonwebtoken.ExpiredJwtException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.util.Objects;

/**
 * Created by JemmyZhang on 2018/3/7
 */
public class UserErrorDecoder implements ErrorDecoder {

    private Logger logger = LoggerFactory.getLogger(UserErrorDecoder.class);

    @Override
    public Exception decode(String s, Response response) {
        try {
            if (Objects.nonNull(response.body())) {
                String body = Util.toString(response.body().asReader());
                DefaultExceptionInfo info = new Gson().fromJson(body, DefaultExceptionInfo.class);

                if (Objects.equals(response.status(), HttpStatus.UNAUTHORIZED.value())
                        || Objects.equals(response.status(), HttpStatus.FORBIDDEN.value())) {
                    return new BasicHttpException(info.getErrorCode(), info.getMessage(), response.status());
                }

                if (StringUtils.isNotEmpty(info.getException())) {
                    Class clazz = Class.forName(info.getException());
                    if (clazz.toString().equals(ExpiredJwtException.class.toString())) {
                        return new BasicHttpException(info.getErrorCode(), info.getMessage(), response.status());
                    }
                }
                return new AppException(info.getErrorCode(), info.getMessage());
            }
        } catch (Exception e) {
            return e;
        }
        return new Exception(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    }
}
