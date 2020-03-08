package com.h3c.vdi.athena.homework.dto;


import java.io.Serializable;
import java.util.List;

/**
 * Created by w16051 on 2018/3/26.
 * 提交作业dto
 */
public class HomeworkSubDTO implements Serializable{

    private static final long serialVersionUID = -7929220668446242479L;

    Long id;

    /**
     * 对应老师发布的作业
     */
    HomeworkDTO homework;

    /**
     * 对应老师发布的作业的名字
     */
    String homeworkName;

    /**
     * 提交作业的用户的ID
     */
    UserDTO user;

    /**
     * 作业最后一次提交时间
     */
    Long submitTime;

    /**
     * 作业批改时间
     */
    Long scoreTime;

    /**
     * 答题栏内容
     */
    String answer;

    /**
     * 分数
     */
    Integer score;

    /**
     * 教师点评
     */
    String comment;

    /**
     * 是否被展示，n：不被展示，y：被展示
     */
    String starred;

    /**
     * n表示未删除，y表示已删除
     */
    String deleted;

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

    public HomeworkDTO getHomework() {
        return homework;
    }

    public void setHomework(HomeworkDTO homework) {
        this.homework = homework;
    }

    public String getHomeworkName() {
        return homeworkName;
    }

    public void setHomeworkName(String homeworkName) {
        this.homeworkName = homeworkName;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public Long getScoreTime() {
        return scoreTime;
    }

    public void setScoreTime(Long scoreTime) {
        this.scoreTime = scoreTime;
    }

    public Long getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Long submitTime) {
        this.submitTime = submitTime;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getStarred() {
        return starred;
    }

    public void setStarred(String starred) {
        this.starred = starred;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
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
