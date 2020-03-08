package com.h3c.vdi.athena.configserver.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author w14014
 * @date 2018/9/27
 */
@Data
public class ConfigDTO implements Serializable{

    private static final long serialVersionUID = -4389809185332905435L;

    /** 文件使用的文件系统 */
    private List<String> fileSystemPath;
    /** 文件系统类型 */
    private String fileSystemType;
    /** 逻辑卷的容量 */
    private Integer capacity;
    /** 文件目录使用情况 */
    private String[] use;
    /** 个人网盘配额 */
    private String quota;
    /** 主机ip */
    private String host;

}
