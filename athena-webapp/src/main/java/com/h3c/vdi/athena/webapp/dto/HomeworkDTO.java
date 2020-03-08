package com.h3c.vdi.athena.webapp.dto;


import java.io.Serializable;
import java.util.List;

/**
 * Created by w16051 on 2018/3/21.
 */
public class HomeworkDTO implements Serializable{
    private static final long serialVersionUID = 7306225138829445185L;

    Long id;

    /**
     * 创建教师的ID
     */
    Long userId;

    /**
     * 所属课程组
     */
    LessonGroupDTO lessonGroupDTO;

    /**
     * 作业名称
     */
    String name;

    /**
     * 作业内容
     */
    String content;

    /**
     * 作业下发时间
     */
    Long createTime;

    /**
     * 作业修改时间
     */
    Long modifiedTime;

    /**
     * 作业提交截止时间
     */
    Long deadLine;

    /**
     * 是否保存为模板
     */
    String isSaved;

    /**
     * 已提交的作业数
     */
    Integer submittedNum;

    /**
     * 已批改作业数
     */
    Integer scoredNum;

    /**
     * 作业平均分
     */
    Integer averageScore;

    /**
     * 作业的附件ids
     */
    List<Long> attachmentIds;

    /**
     * 从网盘传来的作业附件的urls
     */
    List<String> attachmentUrls;

    /**
     * 作业的附件
     */
    List<AttachmentDTO> attachmentDTOS;

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

    public LessonGroupDTO getLessonGroupDTO() {
        return lessonGroupDTO;
    }

    public void setLessonGroupDTO(LessonGroupDTO lessonGroupDTO) {
        this.lessonGroupDTO = lessonGroupDTO;
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

    public Long getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(Long modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public Long getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(Long deadLine) {
        this.deadLine = deadLine;
    }

    public String getIsSaved() {
        return isSaved;
    }

    public void setIsSaved(String isSaved) {
        this.isSaved = isSaved;
    }

    public Integer getSubmittedNum() {
        return submittedNum;
    }

    public void setSubmittedNum(Integer submittedNum) {
        this.submittedNum = submittedNum;
    }

    public Integer getScoredNum() {
        return scoredNum;
    }

    public void setScoredNum(Integer scoredNum) {
        this.scoredNum = scoredNum;
    }

    public Integer getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(Integer averageScore) {
        this.averageScore = averageScore;
    }

    public List<Long> getAttachmentIds() {
        return attachmentIds;
    }

    public void setAttachmentIds(List<Long> attachmentIds) {
        this.attachmentIds = attachmentIds;
    }

    public List<String> getAttachmentUrls() {
        return attachmentUrls;
    }

    public void setAttachmentUrls(List<String> attachmentUrls) {
        this.attachmentUrls = attachmentUrls;
    }

    public List<AttachmentDTO> getAttachmentDTOS() {
        return attachmentDTOS;
    }

    public void setAttachmentDTOS(List<AttachmentDTO> attachmentDTOS) {
        this.attachmentDTOS = attachmentDTOS;
    }
}
