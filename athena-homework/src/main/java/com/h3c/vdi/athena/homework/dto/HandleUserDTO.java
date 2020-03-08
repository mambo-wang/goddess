package com.h3c.vdi.athena.homework.dto;

import java.io.Serializable;

/**
 * Created by w16051 on 2018/10/18.
 */
public class HandleUserDTO implements Serializable {

    private static final long serialVersionUID = 539781080211764106L;

    private Long id;

    private String comments;

    private String event;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
