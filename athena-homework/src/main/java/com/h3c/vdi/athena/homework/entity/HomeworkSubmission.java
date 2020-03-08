package com.h3c.vdi.athena.homework.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by w16051 on 2018/3/26.
 * 提交作业表
 */
@Entity
@Table(name = "tbl_homework_submit")
public class HomeworkSubmission implements Serializable{
    private static final long serialVersionUID = -3887584915338735710L;

    @GeneratedValue
    @Id
    @Column(name = "ID")
    Long id;

    /**
     * 对应老师发布的作业
     */
    @ManyToOne
    @JoinColumn(name = "HOMEWORK_ID")
    Homework homework;

    /**
     * 提交作业的用户的ID
     */
    @Column(name = "USER_ID")
    Long userId;

    /**
     * 作业最后一次提交时间
     */
    @Column(name = "SUBMIT_TIME")
    Long submitTime;

    /**
     * 作业批改时间
     */
    @Column(name = "SCORE_TIME")
    Long scoreTime;

    /**
     * 答题栏内容
     */
    @Column(name = "ANSWER")
    String answer;

    /**
     * 分数
     */
    @Column(name = "SCORE")
    Integer score;

    /**
     * 教师点评
     */
    @Column(name = "COMMENT")
    String comment;

    /**
     * 是否被展示，n：不被展示，y：被展示
     */
    @Column(name = "STARRED")
    String starred;

    /**
     * n表示未删除，y表示已删除
     */
    @Column(name = "IS_DELETED")
    String deleted;

    public Long getHomeworkId(){
        return homework.getId();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Homework getHomework() {
        return homework;
    }

    public void setHomework(Homework homework) {
        this.homework = homework;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Long submitTime) {
        this.submitTime = submitTime;
    }

    public Long getScoreTime() {
        return scoreTime;
    }

    public void setScoreTime(Long scoreTime) {
        this.scoreTime = scoreTime;
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
}
