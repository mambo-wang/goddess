package com.h3c.vdi.athena.keystone.dto;

import lombok.Data;

/**
 *
 * @author w14014
 * @date 2018/9/27
 */
@Data
public class ConfigDTO {

    /** 网盘文件使用的文件系统 */
    private String fileSystemNetDisk;
    /** 网盘文件目录使用情况 */
    private String[] netDiskUse;
    /** 作业附件使用的文件系统 */
    private String fileSystemHomework;
    /** 作业附件目录使用情况 */
    private String[] homeworkUse;
    /** 备份文件使用的文件系统 */
    private String fileSystemBackup;
    /**备份文件目录的使用情况*/
    private String[] backupUse;
    /** 个人网盘配额 */
    private String quota;


}
