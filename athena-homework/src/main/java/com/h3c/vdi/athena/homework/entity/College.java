package com.h3c.vdi.athena.homework.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by z13339 on 2018/3/2.
 */

/**
 * 学院信息表
 */
@Entity
@Table(name = "tbl_college")
public class College implements Serializable {
    private static final long serialVersionUID = 3807026415328732046L;
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

    @Column(name = "IS_DELETED")
    private String deleted;

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
}
