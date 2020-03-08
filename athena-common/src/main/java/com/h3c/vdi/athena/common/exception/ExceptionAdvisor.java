package com.h3c.vdi.athena.common.exception;

import com.h3c.vdi.athena.common.model.DefaultErrorDTO;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletResponse;


/**
 * Created by JemmyZhang on 2018/3/7
 */

@ControllerAdvice
@ResponseBody
public class ExceptionAdvisor {

    private final Logger logger = LoggerFactory.getLogger(ExceptionAdvisor.class);

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public DefaultErrorDTO handleThrowable(Throwable ex) {
        return new DefaultErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public DefaultErrorDTO handleRuntimeException(RuntimeException ex) {
        return new DefaultErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
    }

    @ExceptionHandler(AppException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public DefaultErrorDTO handleAppException(AppException ex) {
        return new DefaultErrorDTO(ex.getErrorCode(), ex.getMessage());
    }

    @ExceptionHandler(BasicHttpException.class)
    public DefaultErrorDTO handleHttpException(BasicHttpException ex, HttpServletResponse response) {
        response.setStatus(ex.getStatus());
        return new DefaultErrorDTO(ex.getErrorCode(), ex.getMessage());
    }

    @ExceptionHandler(HystrixRuntimeException.class)
    public DefaultErrorDTO handleHystrixRuntimeException(HystrixRuntimeException ex, HttpServletResponse response) {
        Throwable cause = ex.getCause();
        if(cause instanceof BasicHttpException){
            response.setStatus(((BasicHttpException)cause).getStatus());
            return new DefaultErrorDTO(((AppException) cause).getErrorCode(), cause.getMessage());
        }
        if (cause instanceof AppException) {
            response.setStatus(HttpStatus.CONFLICT.value());
            return new DefaultErrorDTO(((AppException) cause).getErrorCode(), cause.getMessage());
        }
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new DefaultErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
    }

}
