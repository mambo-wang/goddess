package com.h3c.vdi.athena.common.exception;

/**
 * Created by JemmyZhang on 2018/3/7
 */
public class BasicHttpException extends AppException{

    private int status;

    public BasicHttpException(Integer errorCode, int status) {
        super(errorCode);
        this.status = status;
    }

    public BasicHttpException(String errorMessage, int status) {
        super(errorMessage);
        this.status = status;
    }

    public BasicHttpException(Integer errorCode, String errorMessage, int status) {
        super(errorCode, errorMessage);
        this.status = status;
    }

    public BasicHttpException(Integer errorCode, int status, Object... data) {
        super(errorCode, data);
        this.status = status;
    }

    public BasicHttpException(Integer errorCode, String message, Object[] data, int status) {
        super(errorCode, message, data);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
