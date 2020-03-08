package com.h3c.vdi.athena.webapp.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author w14014
 * @date 2018/9/27
 */
@Data
@ApiModel(value = "参数配置DTO", description = "参数配置DTO")
public class ConfigDTO implements Serializable{

    private static final long serialVersionUID = 1638680728537236527L;

    @ApiModelProperty(value = "使用的文件系统", required = true)
    private List<String> fileSystemPath;
    @ApiModelProperty(value = "使用的文件系统类型", example = "ext4", required = true)
    private String fileSystemType;
    @ApiModelProperty(value = "逻辑卷容量", example = "10", required = true)
    private Integer capacity;
    @ApiModelProperty(value = "文件目录使用情况，[总大小，已使用、剩余可用、使用率]", example = "['75G', '8.7G', '63G', '13%']")
    private String[] use;
    @ApiModelProperty(value = "个人网盘配额, 单位：GB",  example = "5.5G")
    private String quota;
    @ApiModelProperty(value = "服务器IP地址", example = "192.168.3.224")
    private String host;


}
