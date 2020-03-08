package com.h3c.vdi.athena.gateway.exception;

import com.h3c.vdi.athena.common.exception.BasicErrorCode;

/**
 * Created by JemmyZhang on 2018/3/7
 */
public interface ErrorCodes extends BasicErrorCode {

    int DEFAULT_ERROR_NUMBER = 100_200_300;

    /**********  网关服务3开头 **********/

    //登陆认证相关异常
    int LOGIN_ERROR = 300_000_001;
}
