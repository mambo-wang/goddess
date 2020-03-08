package com.h3c.vdi.athena.keystone.exception;

import com.h3c.vdi.athena.common.exception.AppException;
import com.h3c.vdi.athena.common.model.DefaultErrorDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


/**
 * Created by JemmyZhang on 2018/3/7
 */
@ControllerAdvice
@ResponseBody
public class ExceptionAdvisor {

    private final Logger logger= LoggerFactory.getLogger(ExceptionAdvisor.class);

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public DefaultErrorDTO handleThrowable(Throwable ex) {
        logger.info("sss");
        return new DefaultErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public DefaultErrorDTO handleRuntimeException(RuntimeException ex) {
        logger.info("sss");
        return new DefaultErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
    }

    @ExceptionHandler(AppException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public DefaultErrorDTO handleAppException(AppException ex) {
        logger.info("sss");
        return new DefaultErrorDTO(ex.getErrorCode(), ex.getMessage());
    }


}
