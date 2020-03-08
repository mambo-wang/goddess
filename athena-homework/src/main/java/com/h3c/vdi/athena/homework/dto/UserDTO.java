package com.h3c.vdi.athena.homework.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JemmyZhang on 2018/2/24
 */
public class UserDTO implements Serializable{

    private static final long serialVersionUID = 869670840743220949L;

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 登录名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 用户姓名
     */
    private String name;

    /**
     * 头像URL
     */
    private String photo;

    /**
     * 手机号
     */
    private String mobileNo;

    /**
     * Email地址
     */
    private String emailAddress;

    /**
     * 原密码
     */
    private String oldPassword;

    /**
     * 是否为班长
     */
    private boolean isMonitor;

    /**
     * 申请班级的信息
     */
    private ClassEntityDTO registerClass;

    /**
     * 所属班级ID
     */
    private Long classId;

    /**
     * 所属班级编号
     */
    private String classNo;

    /**
     * 批量导入的错误
     */
    private String importError;

    /**
     * 申请班级状态
     */
    private String checkStatus;

    /**
     * 班长最后一次处理的时间
     */
    private Long handleTime;

    /**
     * 班长处理意见
     */
    private String comments;

    /**
     * 上次提交时间
     */
    private Long submitTime;

    private List<RoleDTO> roleList = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getImportError() {
        return importError;
    }

    public void setImportError(String importError) {
        this.importError = importError;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getClassNo() {
        return classNo;
    }

    public void setClassNo(String classNo) {
        this.classNo = classNo;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public List<RoleDTO> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<RoleDTO> roleList) {
        this.roleList = roleList;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public boolean isMonitor() {
        return isMonitor;
    }

    public void setMonitor(boolean monitor) {
        isMonitor = monitor;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public String getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(String checkStatus) {
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

    public Long getSubmitTime() {
        return submitTime;
    }


    public void setSubmitTime(Long submitTime) {
        this.submitTime = submitTime;
    }

    public ClassEntityDTO getRegisterClass() {
        return registerClass;
    }

    public void setRegisterClass(ClassEntityDTO registerClass) {
        this.registerClass = registerClass;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", photo='" + photo + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", oldPassword='" + oldPassword + '\'' +
                ", roleList=" + roleList +
                '}';
    }
}
