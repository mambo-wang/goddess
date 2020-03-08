package com.h3c.vdi.athena.webapp.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author w14014
 * @date 2018/10/13
 */
@Data
@ApiModel(value = "文件系统DTO", description = "查询到服务器中未被挂载文件系统的信息")
public class DiskDTO implements Serializable {
    private static final long serialVersionUID = -1110967554839870250L;

    @ApiModelProperty(value = "路径")
    private String path;

    @ApiModelProperty(value = "空间大小")
    private String size;

    @ApiModelProperty(value = "子分区")
    List<DiskDTO> subDisks;


}
