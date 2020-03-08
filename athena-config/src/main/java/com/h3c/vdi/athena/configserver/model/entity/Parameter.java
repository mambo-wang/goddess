package com.h3c.vdi.athena.configserver.model.entity;

import com.h3c.vdi.athena.common.constant.BasicConstant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

/**
 * 系统参数配置实体。
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Parameter implements Serializable {

    /** 序列化ID。 */
    private static final long serialVersionUID = 1L;

    public static final String TYPE_SYS_CONF = "sys_conf";

    public static final String TYPE_IDLE_TIMEOUT = "idle.timeout";

    public static final String NAME_FILE_SYSTEM_TYPE = "file.system.type";
    public static final String NAME_FILE_SYSTEM_PATH = "file.system.path";
    public static final String NAME_FILE_SYSTEM_CAPACITY = "file.system.capacity";
    public static final String NAME_NETDISK_QUOTA = "netdisk.quota";
    public static final String NAME_HOST_IP = "host.ip";

    public static final String MOUNT_POINT = BasicConstant.MOUNT_POINT;
    public static final String MOUNT_POINT_NEXTCLOUD = BasicConstant.MOUNT_POINT_NEXTCLOUD;
    public static final String MOUNT_POINT_ATTACHMENTS = BasicConstant.MOUNT_POINT_ATTACHMENTS;
    public static final String MOUNT_POINT_NEXTCLOUD_CONFIG = MOUNT_POINT_NEXTCLOUD + "/config";
    public static final String MOUNT_POINT_TMP = "/tmp/athena";

    public static final String VG_ATHENA = "vgathena";
    public static final String LV_ATHENA = "lvathena";

    public static final String MOUNT_FILESYSTEM = "/dev/mapper/" + VG_ATHENA + "-" + LV_ATHENA;


    /** 参数 ID。 */
    private Long id;

    /** 参数类型。 */
    private String type;

    /** 参数名称。 */
    private String name;

    /** 参数值。 */
    private String value;
}
