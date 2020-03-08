package com.h3c.vdi.athena.homework.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by z13339 on 2018/3/2.
 */

/**
 * 班级信息表
 */
@Entity
@Table(name = "tbl_class")
public class ClassEntity implements Serializable {
    private static final long serialVersionUID = -8190779233130876873L;
    /**
     * ID
     */
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 名称
     */
    @Column(name = "NAME")
    private String name;

    /**
     * 班级编号
     */
    @Column(name = "CODE")
    private String code;

    /**
     * 入学时间
     */
    @Column(name = "CREATE_TIME")
    private Date createTime;

    /**
     * 所属学院
     */
    @Column(name = "COLLEGE_ID")
    private Long collegeId;

    /**
     * 班长id
     */
    @Column(name = "MONITOR_ID")
    private Long monitorId;

    @Column(name = "IS_DELETED")
    private String deleted;

    /**
     * 班长邀请码
     * @return
     */
    @Column(name = "INVITE_CODE")
    private Integer inviteCode;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMonitorId() {
        return monitorId;
    }

    public void setMonitorId(Long monitorId) {
        this.monitorId = monitorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getCollegeId() {
        return collegeId;
    }

    public void setCollegeId(Long collegeId) {
        this.collegeId = collegeId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public Integer getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(Integer inviteCode) {
        this.inviteCode = inviteCode;
    }
}
