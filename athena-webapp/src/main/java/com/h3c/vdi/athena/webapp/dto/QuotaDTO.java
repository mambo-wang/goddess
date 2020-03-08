package com.h3c.vdi.athena.webapp.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 *
 * @author w14014
 * @date 2018/9/25
 */
@Data
@ApiModel(value = "网盘用量DTO", description = "当前登录用户其个人网盘的使用情况统计")
public class QuotaDTO {

    @ApiModelProperty(value = "剩余空间大小", example = "6.66 GB")
    private String free;

    @ApiModelProperty(value = "已使用空间大小", example = "3.37 GB")
    private String used;

    @ApiModelProperty(value = "总大小", example = "10.00 GB")
    private String total;

    @ApiModelProperty(value = "使用率", example = "66.66%")
    private String relative;
}
