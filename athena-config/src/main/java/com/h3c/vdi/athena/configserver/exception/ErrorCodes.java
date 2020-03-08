package com.h3c.vdi.athena.configserver.exception;

import com.h3c.vdi.athena.common.exception.BasicErrorCode;

/**
 * Created by JemmyZhang on 2018/2/26
 */
public interface ErrorCodes extends BasicErrorCode {

    int HTTP_INTERNAL_SERVER_ERROR = 100_500_001;

    /****************** 用户相关 1**_1**_*** *******************/
    /**命令执行失败，请确认服务器IP是否配置正确！*/
    int CMD_EXEC_FAIL = 100_100_001;
    /** 物理卷全部创建失败 */
    int PV_CREATE_ALL_FAIL = 100_100_002;


    static String getErrorMessage(final int errorCode, Object... args) {
        return stringManager.getString(PREFIX + errorCode, args);
    }
}
