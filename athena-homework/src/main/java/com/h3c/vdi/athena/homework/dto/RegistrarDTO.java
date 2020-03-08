package com.h3c.vdi.athena.homework.dto;

import com.h3c.vdi.athena.homework.stateMachine.enums.CheckStatus;

import java.io.Serializable;

/**
 * Created by w16051 on 2018/3/2.
 * 用来接收注册用户的实体
 */
public class RegistrarDTO implements Serializable{

    private static final long serialVersionUID = -8258531443837134815L;

    private Long id;

    private Long userId;

    private String username;

    private String password;

    private String name;

    /**
     * 手机号
     */
    private String mobileNo;

    private String emailAddress;

    private Long classId;

    private Integer inviteCode;

    /**
     * 注册申请提交时间
     */
    private Long submitTime;

    /**
     * 处理状态
     */
    private CheckStatus checkStatus;

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

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
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

    public Integer getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(Integer inviteCode) {
        this.inviteCode = inviteCode;
    }
}
