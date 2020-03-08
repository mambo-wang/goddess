package com.h3c.vdi.athena.configserver.model.entity;

import lombok.Data;

/**
 *
 * 用于设置开机自动挂载
 * @author w14014
 * @date 2018/9/28
 */
@Deprecated
@Data
public class MountInfo {

    private Long id;
    /** 实际分区名 */
    private String fileSystem;
    /** 挂载点*/
    private String mountPoint;
    /** 文件系统类型 */
    private String type;
    /** 挂载选项 */
    private String options;
    /** dump备份设置，0:忽略备份  1：允许dump程序备份 */
    private Integer dump;
    /**fsck检查   0不检查  1检查*/
    private Integer pass;

}
