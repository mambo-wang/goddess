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
@ApiModel(value = "文件共享信息DTO", description = "只包含某一次共享，表示文件和用户或分组一对一的关联")
public class ShareDTO {

    @ApiModelProperty(value = "1：分享给别人   2：别人分享给我")
    private Integer shared;

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "分享的类型：0：分享给用户，1或者2：分享给分组")
    private Integer shareType;

    @ApiModelProperty(value = "文件所有者的用户名")
    private String uIdOwner;

    @ApiModelProperty(value = "文件所有者的姓名")
    private String displayNameOwner;

    @ApiModelProperty(value = "发起分享的时间")
    private Long sTime;

    public String uIdFileOwner;

    public String displayNameFileOwner;

    @ApiModelProperty(value = "文件路径（相对路径）")
    private String path;

    @ApiModelProperty(value = "文件类型：file:文件，folder:文件夹")
    private String itemType;

    @ApiModelProperty(value = "文件路径（相对路径），暂时不用")
    private String fileTarget;

    @ApiModelProperty(value = "分享给谁，被分享人的用户名")
    private String shareWith;

    @ApiModelProperty(value = "被分享人的姓名")
    private String shareWithDisplayName;

    @ApiModelProperty(value = "被分享人的分组信息")
    private String shareWithGroupInfo;
}
