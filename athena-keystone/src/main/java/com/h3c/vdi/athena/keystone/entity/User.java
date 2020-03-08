package com.h3c.vdi.athena.keystone.entity;

import com.sun.org.apache.xerces.internal.impl.dv.dtd.NOTATIONDatatypeValidator;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Created by JemmyZhang on 2018/2/13
 */
@Entity(name = "tbl_user")
public class User implements Serializable {

    public static final long serialVersionUID = 5922853920507670510L;

    public static final String FIELD_ID = "id";

    public static final String FIELD_USERNAME = "username";

    public static final String FIELD_PASSWORD = "password";

    public static final String FIELD_NAME = "name";

    public static final String FIELD_PHOTO = "photo";

    public static final String FIELD_EMAIL_ADDRESS = "emailAddress";

    public static final String FIELD_IS_DELETED = "deleted";

    public static final String NOT_DELETED = "n";

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "NAME")
    private String name;

    @Column(name = "PHOTO")
    private String photo;

    @Column(name = "MOBILE_NO")
    String mobileNo;

    @Column(name = "EMAIL_ADDRESS")
    private String emailAddress;

    @Column(name = "IS_DELETED")
    private String deleted = NOT_DELETED;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "tbl_user_role", joinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
            , inverseJoinColumns = @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID"))
    private List<Role> roles;

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

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
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

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
