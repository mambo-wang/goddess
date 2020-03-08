package com.h3c.vdi.athena.homework.entity;

import com.h3c.vdi.athena.homework.stateMachine.enums.CheckStatus;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by w16051 on 2018/3/8.
 * 注册用户表，用来保存需要审核的用户的信息
 */
@Entity
@Table(name = "tbl_registrar")
public class Registrar implements Serializable{

    private static final long serialVersionUID = 3352639900003212413L;

    @Id
    @Column(name = "ID")
    @GeneratedValue
    Long id;

    @Column(name = "USER_ID")
    Long userId;

    @Column(name="USERNAME")
    String username;

    @Column(name = "PASSWORD")
    String password;

    @Column(name="NAME")
    String name;

    @Column(name="EMAIL_ADDRESS")
    String emailAddress;

    @Column(name = "MOBILE_NO")
    String mobileNo;

    @Column(name = "CLASS_ID")
    Long classId;

    /**
     * 注册申请提交时间
     */
    @Column(name="SUBMIT_TIME")
    private Long submitTime;

    /**
     * 处理状态
     */
    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private CheckStatus checkStatus;

    @Column(name = "HANDLE_TIME")
    private Long handleTime;

    @Column(name = "COMMENTS")
    private String comments;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public Long getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Long submitTime) {
        this.submitTime = submitTime;
    }

    public CheckStatus getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(CheckStatus checkStatus) {
        this.checkStatus = checkStatus;
    }

    public Long getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(Long handleTime) {
        this.handleTime = handleTime;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
