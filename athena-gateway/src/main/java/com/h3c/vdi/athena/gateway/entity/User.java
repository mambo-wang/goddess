package com.h3c.vdi.athena.gateway.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by JemmyZhang on 2018/2/13
 */
public class User implements Serializable {

    private static final long serialVersionUID = 5922853920507670510L;

    private Long id;

    private String loginName;

    private String userName;

    private String password;

    private String photo;

    private String emailAddress;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
