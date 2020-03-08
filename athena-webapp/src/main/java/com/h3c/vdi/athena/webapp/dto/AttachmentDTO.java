package com.h3c.vdi.athena.webapp.dto;

import java.io.Serializable;

/**
 * Created by w16051 on 2018/3/27.
 * 附件DTO
 */
public class AttachmentDTO implements Serializable{

    private static final long serialVersionUID = 5050866655744460818L;

    Long id;

    /**
     * 附件名称
     */
    String name;

    /**
     * 附件所关联的表的数据类型，0：学生作业，1：教师布置作业，2：教师模板
     */
    Integer type;

    /**
     * 关联表的ID，有学生作业表，教师作业表和教师模板表三种可能
     */
    Long relatedId;

    /**
     * 资源位置
     */
    String url;


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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(Long relatedId) {
        this.relatedId = relatedId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
