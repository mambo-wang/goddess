package com.h3c.vdi.athena.webapp.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author w14014
 * @date 2018/10/19
 */
@Data
@ApiModel(value = "可用分区和可选文件系统类型")
public class PreDataDTO implements Serializable{

    private static final long serialVersionUID = -2422785469354353L;
    @ApiModelProperty(value = "可选的文件系统类型")
    List<String> fileSystemTypes;

    @ApiModelProperty(value = "可用分区")
    List<DiskDTO> disks;
}
