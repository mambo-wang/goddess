package com.h3c.vdi.athena.homework.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by z13339 on 2018/3/20.
 */
@Entity
@Table(name = "tbl_comment")
public class Comment implements Serializable {

    private static final long serialVersionUID = 4108212847307154589L;

    @Id
    @GeneratedValue
    private Long id;

    /**
     * 1：学生作业，2：老师作业
     */
    @Column(name = "TYPE")
    private Integer type;

    /**
     * 作业ID，有可能是老师作业，也有可能是学生作业
     */
    @Column(name = "RELATION_ID")
    private Long relationId;

    /**
     * 评论的内容
     */
    @Column(name = "CONTENT")
    private String content;

    /**
     *提交评论的时间
     */
    @Column(name = "CREATE_TIME")
    private Long createTime;

    /**
     * 用户主键ID
     */
    @Column(name = "USER_ID")
    private Long userId;

    /**
     * 楼层
     */
    @Column(name = "FLOOR")
    private Integer floor;

    /**
     * 回复的对象，楼层加用户姓名，形如“x楼xxx”
     */
    @Column(name = "TARGET")
    private String target;

    /**
     * n表示未删除，y表示已删除
     */
    @Column(name = "IS_DELETED")
    private String deleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getRelationId() {
        return relationId;
    }

    public void setRelationId(Long relationId) {
        this.relationId = relationId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }
}
