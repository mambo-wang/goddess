package com.h3c.vdi.athena.homework.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by w14014 on 2018/3/8.
 */
@Entity
@Table(name = "TBL_USER_GROUP")
public class UserGroupRelation implements Serializable{
    private static final long serialVersionUID = -3967048796305658289L;

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "GROUP_ID")
    private Long groupId;

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

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "UserGroupRelation{" +
                "id=" + id +
                ", userId=" + userId +
                ", groupId=" + groupId +
                ", deleted='" + deleted + '\'' +
                '}';
    }
}
