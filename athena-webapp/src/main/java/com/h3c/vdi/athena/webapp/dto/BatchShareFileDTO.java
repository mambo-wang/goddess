package com.h3c.vdi.athena.webapp.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author w14014
 * @date 2018/9/26
 */
@Data
@ApiModel(value = "文件批量分享请求DTO", description = "用来发起文件分享，分享给多个用户")
public class BatchShareFileDTO implements Serializable{

    private static final long serialVersionUID = 7124212910482617251L;
    @ApiModelProperty(value = "分享给谁，被分享者的用户名", required = true)
    List<String> shareWith;

    @ApiModelProperty(value = "文件的相对路径", required = true)
    List<String> path;
}
