package com.h3c.vdi.athena.webapp.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author w14014
 * @date 2018/9/25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "模糊查询结果DTO", description = "模糊查询的结果")
public class SearchResultDTO {

    @ApiModelProperty(value = "用户或者分组名")
    private String name;

    /**0:个人用户，1：分组*/
    @ApiModelProperty(value = "结果类型：0：个人用户，1：分组")
    private Integer type;
}
