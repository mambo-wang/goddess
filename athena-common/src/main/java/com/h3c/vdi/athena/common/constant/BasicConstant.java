package com.h3c.vdi.athena.common.constant;

import java.nio.charset.Charset;
import java.time.ZoneId;

/**
 * Created by JemmyZhang on 2018/3/1
 */
public interface BasicConstant {

    String IS_DELETED_N="n";

    String IS_DELETED_Y="y";

    String SYS_ARTIFACT_ID="athena";

    Charset CHARSET_UTF8 = Charset.forName("UTF-8");

    Charset CHARSET_GBK = Charset.forName("GBK");

    ZoneId BEIJING_ZONE = ZoneId.of("UTC+08:00");

    String MOUNT_POINT = "/var/file";
    String MOUNT_POINT_NEXTCLOUD = MOUNT_POINT + "/nextcloud";
    String MOUNT_POINT_ATTACHMENTS = MOUNT_POINT + "/attachments";
}
