package com.h3c.vdi.athena.homework.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by w16051 on 2018/3/21.
 * 教师下发作业表
 */
@Entity
@Table(name = "tbl_homework")
public class Homework implements Serializable{

    private static final long serialVersionUID = 6088502619557452809L;

    @Id
    @GeneratedValue
    @Column(name="ID")
    Long id;

    /**
     * 创建教师的ID
     */
    @Column(name="USER_ID")
    Long userId;

    /**
     * 所属课程组
     */
    @ManyToOne
    @JoinColumn(name="GROUP_ID")
    LessonGroup lessonGroup;

    /**
     * 作业名称
     */
    @Column(name = "NAME")
    String name;

    /**
     * 作业内容
     */
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "CONTENT",columnDefinition = "TEXT")
    String content;

    /**
     * 作业下发时间
     */
    @Column(name = "CREATE_TIME")
    Long createTime;

    /**
     * 作业修改时间
     */
    @Column(name = "MODIFIED_TIME")
    Long modifiedTime;

    /**
     * 作业提交截止时间
     */
    @Column(name = "DEADLINE")
    Long deadLine;

    /**
     * 是否保存为模板
     */
    @Column(name = "IS_SAVED")
    String saved;

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LessonGroup getLessonGroup() {
        return lessonGroup;
    }

    public void setLessonGroup(LessonGroup lessonGroup) {
        this.lessonGroup = lessonGroup;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Long getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(Long modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public Long getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(Long deadLine) {
        this.deadLine = deadLine;
    }

    public String getSaved() {
        return saved;
    }

    public void setSaved(String saved) {
        this.saved = saved;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }
}
