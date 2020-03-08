package com.h3c.vdi.athena.webapp.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author w14014
 * @date 2018/10/18
 */
@Data
@ApiModel("移动/复制文件DTO")
public class MoveCopyDTO implements Serializable{

    private static final long serialVersionUID = 5365912064533694587L;

    @ApiModelProperty(value = "源文件路径")
    List<String> srcPath;

    @ApiModelProperty(value = "目标文件夹路径")
    String destPath;

}
