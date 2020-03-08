package com.h3c.vdi.athena.netdisk.exception;

import com.h3c.vdi.athena.common.exception.BasicErrorCode;

/**
 * Created by JemmyZhang on 2018/2/26
 */
public interface ErrorCodes extends BasicErrorCode {

    int HTTP_INTERNAL_SERVER_ERROR = 100_500_001;

    /****************** 用户相关 1**_1**_*** *******************/
    /**修改配额失败*/
    int USER_MODIFY_QUOTA_FAIL = 100_100_001;
    /**网盘连接失败，请确认redis服务是否正常，然后重新登陆*/
    int USER_PASSWORD_GET_FAIL = 100_100_002;
    /** 用户创建失败 */
    int USER_CREATE_FAIL = 100_100_003;
    /** 用户组创建失败 */
    int USER_GROUP_CREATE_FAIL = 100_100_004;

    /*********************网盘相关 1**_2**_*** ***************************************/
    /** 单文件下载失败 */
    int NETDISK_DOWNLOAD_SINGLE_FAIL = 100_200_001;
    /** 文件夹下载失败 */
    int NETDISK_DOWNLOAD_MULTI_FAIL = 100_200_002;
    /** 文件删除失败 */
    int NETDISK_DELETE_FAIL = 100_200_003;
    /** 文件分类查询失败 */
    int NETDISK_QUERY_TYPE_FAIL = 100_200_004;
    /** 文件移动失败 */
    int NETDISK_MOVE_FAIL = 100_200_005;
    /** 文件复制失败 */
    int NETDISK_COPY_FAIL = 100_200_006;
    /** 文件上传失败 */
    int NETDISK_UPLOAD_FAIL = 100_200_007;
    /** 文件不存在 */
    int NETDISK_FILE_NOT_FOUND = 100_200_008;


    static String getErrorMessage(final int errorCode, Object... args) {
        return stringManager.getString(PREFIX + errorCode, args);
    }
}
