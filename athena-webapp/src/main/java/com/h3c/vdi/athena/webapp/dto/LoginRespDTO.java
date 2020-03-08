package com.h3c.vdi.athena.webapp.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 *
 * @author w14014
 * @date 2018/10/13
 */
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@ApiModel(value = "登陆返回体", description = "用户登陆请求返回的数据")
public class LoginRespDTO {
    @ApiModelProperty(value = "token字符串")
    private String token;
    @ApiModelProperty(value = "用户DTO")
    private UserDTO user;
}
