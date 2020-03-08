package com.h3c.vdi.athena.homework.dto;

public class DataCensusScoresDTO {

    /**
     * 作业名称
     */
    private String name;

    /**
     * 作业分数
     */
    private String score;

    public DataCensusScoresDTO() {
    }

    public DataCensusScoresDTO(String name, String score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
