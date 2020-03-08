package com.h3c.vdi.athena.webapp.dto;

import java.io.Serializable;

/**
 * Created by w16051 on 2018/4/8.
 * 用来传递展示作业的信息
 */
public class ShowHomeworkInfoDTO implements Serializable {
    private static final long serialVersionUID = -2799071639537928707L;

    /**
     *提交作业的id
     */
    private Long homeworkSubId;

    /**
     * 是否展示
     */
    private String status;

    public Long getHomeworkSubId() {
        return homeworkSubId;
    }

    public void setHomeworkSubId(Long homeworkSubId) {
        this.homeworkSubId = homeworkSubId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
