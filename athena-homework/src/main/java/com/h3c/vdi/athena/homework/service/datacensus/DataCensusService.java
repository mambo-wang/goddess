package com.h3c.vdi.athena.homework.service.datacensus;

import com.h3c.vdi.athena.homework.dto.DataCensusDTO;
import com.h3c.vdi.athena.homework.dto.DataCensusScoresDTO;

import java.util.List;

public interface DataCensusService {

    /**
     * 根据老师和其课程、时间段查询学生作业完成情况，以列表展示
     *
     * @param lessonGroupId 课程组名称
     * @param startDate       开始时间
     * @param endDate         结束时间
     * @return
     */
    List<DataCensusDTO> queryHomeworkCompleteResult(Long lessonGroupId, Long startDate, Long endDate);

    /**
     * 查询某次作业全部学生的平均分，以折线图展示
     *
     * @param lessonGroupName 课程组名称
     * @param startDate       开始时间
     * @param endDate         结束时间
     * @return
     */
    List<DataCensusScoresDTO> queryAverageScore(Long lessonGroupName, Long startDate, Long endDate);

    /**
     * 查询所有(某次)作业的完成情况的百分比
     *
     * @param lessonGroupId 课程组名称
     * @param homeworkId    作业名称，可以为空，为空时查询所有作业；不为空时查询当前作业
     * @param startDate       开始时间
     * @param endDate         结束时间
     * @return 完成情况的百分比（优秀、良好、中等、及格、不及格、未完成）
     */
    List<Double> teacherViewCompletePercent(Long lessonGroupId, Long homeworkId, Long startDate, Long endDate);

    /**
     * 展示某学生所有课程或某个课程某个时间段内的得分情况，饼状图，学生视图
     *
     * @param lessonGroupId 课程组名称
     * @param startDate       开始时间
     * @param endDate         结束时间
     * @return 完成情况的百分比（优秀、良好、中等、及格、不及格、未完成）
     */
    List<Double> studentViewCompletePercent(Long lessonGroupId, Long startDate, Long endDate);

    /**
     * 展示某学生某课程某时间段内的作业完成情况，折线图
     * @param lessonGroupId 课程组名称
     * @param startDate       开始时间
     * @param endDate         结束时间
     * @return 作业和分数集合
     */
    List<DataCensusScoresDTO> queryStudentScore(Long lessonGroupId, Long startDate, Long endDate);
}
