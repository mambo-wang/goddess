package com.h3c.vdi.athena.homework.dto;

import java.io.Serializable;

/**
 * Created by JemmyZhang on 2018/2/26
 */
public class ErrorDTO implements Serializable {
    private static final long serialVersionUID = -2509236891222414535L;


    private int errorCode = 0;

    private String errorMessage;

    public ErrorDTO(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public ErrorDTO(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
