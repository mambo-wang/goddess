package com.h3c.vdi.athena.webapp.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;

/**
 *
 * @author w14014
 * @date 2018/9/19
 */
@Data
@ApiModel(value = "文件/文件夹信息DTO", description = "文件或文件夹的详细信息，包括其共享信息")
public class FileInfoDTO {
    @ApiModelProperty(value = "文件名称")
    private String name;
    @ApiModelProperty(value = "文件路径", example = "/folder1/demo.txt")
    private String path;
    @ApiModelProperty(value = "文件类型， 0：文件夹，1：文档，2：音乐，3：视频， 4：图片，5：压缩包，6：其他")
    private Integer type;
    @ApiModelProperty(value = "文件大小，字符串，前端直接展示")
    private String contentLength;
    @ApiModelProperty(value = "文件最近一次修改时间")
    private String modified;
    @ApiModelProperty(value = "文件的绝对路径")
    private String absolutePath;

    /**0:未分享   1：分享给别人   2：别人分享给我*/
    @ApiModelProperty(value = "1 我分享给别人，2 别人分享给我")
    private Integer shared;
    @ApiModelProperty(value = "分享信息，可能有多个")
    private List<ShareDTO> shares;

    /**查询目录树的时候使用*/
    @ApiModelProperty(value = "子文件/文件夹列表，仅在复制/移动/选择网盘文件交作业时有值")
    private List<FileInfoDTO> subFiles;
}
