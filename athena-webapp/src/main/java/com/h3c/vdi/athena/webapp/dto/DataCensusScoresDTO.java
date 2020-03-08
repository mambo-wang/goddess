package com.h3c.vdi.athena.webapp.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author w14014
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "作业得分DTO")
public class DataCensusScoresDTO implements Serializable{

    private static final long serialVersionUID = -3317818436155536149L;
    /**
     * 作业名称
     */
    @ApiModelProperty(value = "作业名称")
    private String name;

    /**
     * 作业分数
     */
    @ApiModelProperty(value = "分数")
    private String score;
}
