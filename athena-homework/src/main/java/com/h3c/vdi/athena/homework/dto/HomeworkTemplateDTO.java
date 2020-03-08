package com.h3c.vdi.athena.homework.dto;

import java.util.Date;
import java.util.List;

public class HomeworkTemplateDTO {

    private Long id;

    private Long userId;

    private String name;

    private String content;

    private Long createTime;

    private String isDeleted;

    /**
     * 作业的附件ids
     */
    private List<Long> attachmentIds;

    /**
     * 作业的附件
     */
    private List<AttachmentDTO> attachmentDTOS;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public List<Long> getAttachmentIds() {
        return attachmentIds;
    }

    public void setAttachmentIds(List<Long> attachmentIds) {
        this.attachmentIds = attachmentIds;
    }

    public List<AttachmentDTO> getAttachmentDTOS() {
        return attachmentDTOS;
    }

    public void setAttachmentDTOS(List<AttachmentDTO> attachmentDTOS) {
        this.attachmentDTOS = attachmentDTOS;
    }
}
