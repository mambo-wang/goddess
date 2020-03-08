package com.h3c.vdi.athena.homework.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by z13339 on 2018/4/24.
 */
@Entity
@Table(name = "tbl_homework_submit")
public class HomeworkPraise implements Serializable{

    private static final long serialVersionUID = -6103003140545398729L;

    @GeneratedValue
    @Id
    @Column(name = "ID")
    Long id;

    @Column(name = "HOMEWORK_SUB_ID")
    Long homeworkSubId;

    @Column(name = "USER_ID")
    Long userId;


    @Column(name = "CREATE_TIME")
    Long creatTime;


    @Column(name = "MODIFIED_TIME")
    Long ModifiedTime;

    /**
     * n表示未删除，y表示已删除
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

    public Long getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(Long creatTime) {
        this.creatTime = creatTime;
    }

    public Long getModifiedTime() {
        return ModifiedTime;
    }

    public void setModifiedTime(Long modifiedTime) {
        ModifiedTime = modifiedTime;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public Long getHomeworkSubId() {
        return homeworkSubId;
    }

    public void setHomeworkSubId(Long homeworkSubId) {
        this.homeworkSubId = homeworkSubId;
    }
}
