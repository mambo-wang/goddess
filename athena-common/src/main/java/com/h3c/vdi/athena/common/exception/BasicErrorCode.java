package com.h3c.vdi.athena.common.exception;

import com.h3c.vdi.athena.common.utils.StringManager;

import java.util.Objects;

/**
 * Created by JemmyZhang on 2018/2/26
 */
public interface BasicErrorCode {

    StringManager stringManager = StringManager.getManager(BasicErrorCode.RESOURCE_LOCATION);

    String RESOURCE_LOCATION = "ErrorCodes";

    String PREFIX = "errorCodes.";

    int DEFAULT_UNDEFINED_ERROR = 0;

    static String getErrorMessage(Integer errorCode, Object... args) {
        return stringManager.getString(PREFIX + (Objects.isNull(errorCode) ? DEFAULT_UNDEFINED_ERROR : errorCode), args);
    }
}
