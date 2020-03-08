package com.h3c.vdi.athena.homework.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by w14014 on 2018/3/8.
 *
 * 课程组实体
 */
@Entity
@Table(name = "TBL_LESSON_GROUP")
public class LessonGroup implements Serializable{

    private static final long serialVersionUID = -907513079721645285L;

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "NAME")
    private String name;

    @Column(name = "MEMBER_LIMIT")
    private Integer memberLimit;

    @Column(name = "IS_DELETED")
    private String deleted;

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

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "LessonGroup{" +
                "id=" + id +
                ", userId=" + userId +
                ", name='" + name + '\'' +
                ", memberLimit=" + memberLimit +
                ", deleted='" + deleted + '\'' +
                '}';
    }
}
