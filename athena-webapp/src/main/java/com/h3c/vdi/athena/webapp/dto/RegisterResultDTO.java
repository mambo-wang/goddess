package com.h3c.vdi.athena.webapp.dto;

import java.io.Serializable;

/**
 * Created by w16051 on 2018/3/8.
 */
public class RegisterResultDTO implements Serializable{

    private static final long serialVersionUID = -3892824717914427489L;

    private String result;

    private Long monitorId;

    private String monitorName;

    private String error;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Long getMonitorId() {
        return monitorId;
    }

    public void setMonitorId(Long monitorId) {
        this.monitorId = monitorId;
    }

    public String getMonitorName() {
        return monitorName;
    }

    public void setMonitorName(String monitorName) {
        this.monitorName = monitorName;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
