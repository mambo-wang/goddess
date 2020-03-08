package com.h3c.vdi.athena.common.exception;

import com.netflix.hystrix.exception.HystrixBadRequestException;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by JemmyZhang on 2018/2/26
 */
public class AppException extends HystrixBadRequestException implements Serializable {

    private static final long serialVersionUID = 2860991341559790081L;

    private Integer errorCode = BasicErrorCode.DEFAULT_UNDEFINED_ERROR;

    public AppException(Integer errorCode) {
        super(buildErrorMessage(errorCode));
        this.errorCode = errorCode;
    }

    public AppException(String errorMessage) {
        super(errorMessage);
    }

    public AppException(Integer errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
    }

    public AppException(Integer errorCode, Object... data) {
        super(buildErrorMessage(errorCode, data));
        this.errorCode = errorCode;
    }

    public AppException(Integer errorCode, String message, Object[] data) {
        super(buildErrorMessage(errorCode, data));
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        String message = super.getMessage();
        return Objects.isNull(message) ? getErrorMessage() : message;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage(Object... data) {
        return buildErrorMessage(errorCode, data);
    }

    private static String buildErrorMessage(Integer errorCode, Object... data) {
        return BasicErrorCode.getErrorMessage(errorCode, data);
    }
}
