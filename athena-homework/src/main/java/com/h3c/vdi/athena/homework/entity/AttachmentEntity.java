package com.h3c.vdi.athena.homework.entity;

import javax.persistence.*;
import java.io.Serializable;


/**
 * Created by w16051 on 2018/3/27.
 * 附件表
 */
@Entity
@Table(name = "tbl_attachments")
public class AttachmentEntity implements Serializable{

    private static final long serialVersionUID = -5471548928100255380L;

    @Id
    @GeneratedValue
    @Column(name = "ID")
    Long id;

    /**
     * 附件名称
     */
    @Column(name = "NAME")
    String name;

    /**
     * 附件所关联的表的数据类型，0：学生作业，1：教师布置作业，2：教师模板
     */
    @Column(name = "TYPE")
    Integer type;

    /**
     * 关联表的ID，有学生作业表，教师作业表和教师模板表三种可能
     */
    @Column(name = "RELATED_ID")
    Long relatedId;

    /**
     * 资源位置
     */
    @Column(name = "URL")
    String url;

    /**
     * 是否删除
     */
    @Column(name = "IS_DELETED")
    String deleted;

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

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }
}
