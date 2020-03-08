package com.h3c.vdi.athena.homework.dto;

import java.io.Serializable;

/**
 * Created by w16051 on 2018/3/2.
 * 角色DTO
 */
public class RoleDTO implements Serializable{

    private static final long serialVersionUID = -4948973756212627033L;

    /**
     * 角色ID
     */
    private Long id;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色所属系统名称
     */
    private String sysName;

    /**
     * 中文描述信息
     */
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
