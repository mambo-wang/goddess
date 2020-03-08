package com.h3c.vdi.athena.homework.dto;

import com.h3c.vdi.athena.homework.entity.HomeworkSubmission;

import java.io.Serializable;

/**
 * Created by z13339 on 2018/4/24.
 */
public class HomeworkPraiseDTO implements Serializable {

    private static final long serialVersionUID = 1883352863024388131L;

    Long id;

    HomeworkSubDTO homeworkSubDTO;

    Long UserId;


    Long creatTime;


    Long ModifiedTime;

    String deleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public HomeworkSubDTO getHomeworkSubDTO() {
        return homeworkSubDTO;
    }

    public void setHomeworkSubDTO(HomeworkSubDTO homeworkSubDTO) {
        this.homeworkSubDTO = homeworkSubDTO;
    }

    public Long getUserId() {
        return UserId;
    }

    public void setUserId(Long userId) {
        UserId = userId;
    }

    public Long getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(Long creatTime) {
        this.creatTime = creatTime;
    }

    public Long getModifiedTime() {
        return ModifiedTime;
    }

    public void setModifiedTime(Long modifiedTime) {
        ModifiedTime = modifiedTime;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }
}
