package com.h3c.vdi.athena.homework.dto;

import java.util.List;

public class DataCensusDTO {

    private String userInfo;

    private List<DataCensusScoresDTO> scores;

    private Double averageScore;

    private int excellentCount;

    public String getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(String userInfo) {
        this.userInfo = userInfo;
    }

    public List<DataCensusScoresDTO> getScores() {
        return scores;
    }

    public void setScores(List<DataCensusScoresDTO> scores) {
        this.scores = scores;
    }

    public Double getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(Double averageScore) {
        this.averageScore = averageScore;
    }

    public int getExcellentCount() {
        return excellentCount;
    }

    public void setExcellentCount(int excellentCount) {
        this.excellentCount = excellentCount;
    }
}
