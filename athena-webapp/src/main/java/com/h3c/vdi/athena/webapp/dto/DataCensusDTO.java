package com.h3c.vdi.athena.webapp.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "数据统计信息DTO")
public class DataCensusDTO implements Serializable{

    private static final long serialVersionUID = -8975890364514491547L;
    @ApiModelProperty(value = "用户姓名")
    private String userInfo;
    @ApiModelProperty(value = "分数信息")
    private List<DataCensusScoresDTO> scores;
    @ApiModelProperty(value = "平均分")
    private Double averageScore;
    @ApiModelProperty(value = "优秀作业次数")
    private int excellentCount;
}
