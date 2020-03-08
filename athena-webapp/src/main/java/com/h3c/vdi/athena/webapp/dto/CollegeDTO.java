package com.h3c.vdi.athena.webapp.dto;

import java.io.Serializable;

/**
 * Created by z13339 on 2018/3/2.
 */
public class CollegeDTO implements Serializable {

    private static final long serialVersionUID = -3048315709663175621L;
    private Long id;
    private String name;

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
}
