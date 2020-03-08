package com.h3c.vdi.athena.common.model;

import java.io.Serializable;

/**
 * Created by JemmyZhang on 2018/2/26
 */
public class DefaultErrorDTO implements Serializable {

    private static final long serialVersionUID = -2509236891222414535L;

    private Integer errorCode;

    private String message;

    public DefaultErrorDTO(String message) {
        this.message = message;
    }

    public DefaultErrorDTO(Integer errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
