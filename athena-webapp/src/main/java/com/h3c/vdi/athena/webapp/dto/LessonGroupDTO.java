package com.h3c.vdi.athena.webapp.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author w14014
 * @date 2018/3/8
 */
@ApiModel(value = "课程组信息")
public class LessonGroupDTO implements Serializable{
    private static final long serialVersionUID = 8032832110117464680L;

    private Long id;

    private UserDTO user;

    private String name;

    @ApiModelProperty(value = "限制人数")
    private Integer memberLimit;

    @ApiModelProperty(value = "课程组里的用户，只在文件分享时有值")
    private List<UserDTO> users;

    public List<UserDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UserDTO> users) {
        this.users = users;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMemberLimit() {
        return memberLimit;
    }

    public void setMemberLimit(Integer memberLimit) {
        this.memberLimit = memberLimit;
    }
}
