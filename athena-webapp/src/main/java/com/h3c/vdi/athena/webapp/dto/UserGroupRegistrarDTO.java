package com.h3c.vdi.athena.webapp.dto;

import com.h3c.vdi.athena.webapp.enums.CheckStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 *
 * @author w14014
 * @date 2018/10/16
 */
@Data
@ApiModel(value = "加入课程组申请信息")
public class UserGroupRegistrarDTO implements Serializable{

    private static final long serialVersionUID = 5384607388149177868L;

    @ApiModelProperty(value = "申请信息主键id")
    private Long id;

    @ApiModelProperty(value = "学生信息")
    private UserDTO user;

    @ApiModelProperty(value = "课程组id")
    private Long groupId;

    @ApiModelProperty(value = "课程组名称")
    private String groupName;

    @ApiModelProperty(value = "提交时间")
    private Long submitTime;

    @ApiModelProperty(value = "状态，UNCHECKED:未处理，PASSED:通过，UNPASSED:拒绝")
    private CheckStatus checkStatus;

}
