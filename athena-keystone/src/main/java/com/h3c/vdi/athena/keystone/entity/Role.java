package com.h3c.vdi.athena.keystone.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by JemmyZhang on 2018/3/1
 */
@Entity(name = "tbl_role")
public class Role implements Serializable{

    private static final long serialVersionUID = -7000355767871253528L;

    public static final String LOC_SYS = "athena";

    public static final String ROLE_TEACHER = "TEACHER";

    public static final String ROLE_STUDENT = "STUDENT";
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "SYS")
    private String sysName;

    @Column(name = "IS_DELETED")
    private String deleted = "n";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }
}
