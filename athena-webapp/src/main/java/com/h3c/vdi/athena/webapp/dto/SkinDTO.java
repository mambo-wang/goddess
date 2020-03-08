package com.h3c.vdi.athena.webapp.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 *
 * @author w14014
 * @date 2018/10/13
 */
@Setter
@ToString
@Getter
@ApiModel(value = "皮肤配置DTO")
public class SkinDTO implements Serializable{

    private static final long serialVersionUID = 8672242511386983889L;
    @ApiModelProperty(value = "用户名")
    private String username;
    @ApiModelProperty(value = "皮肤编号")
    private Integer skinNumber;
}
