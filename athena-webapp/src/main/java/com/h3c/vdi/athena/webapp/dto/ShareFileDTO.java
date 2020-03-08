package com.h3c.vdi.athena.webapp.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * @author w14014
 * @date 2018/9/26
 */
@Data
@ApiModel(value = "文件分享请求DTO", description = "用来发起文件分享")
public class ShareFileDTO {

    @ApiModelProperty(value = "分享的类型：0：分享给用户，1或者2：分享给分组", required = true)
    Integer shareType;

    @ApiModelProperty(value = "分享给谁，被分享者的用户名", required = true)
    String shareWith;

    @ApiModelProperty(value = "文件的相对路径", required = true)
    String path;
}
