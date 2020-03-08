package com.h3c.vdi.athena.homework.controller;

import com.h3c.vdi.athena.homework.dto.DataCensusDTO;
import com.h3c.vdi.athena.homework.dto.DataCensusScoresDTO;
import com.h3c.vdi.athena.homework.service.datacensus.DataCensusService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 数据统计
 */
@RestController
@RequestMapping("/statistics")
public class DataCensusController {

    @Resource
    private DataCensusService dataCensusService;

    /**
     * 根据老师和其课程、时间段查询学生作业完成情况，以列表展示
     *
     * @param lessonGroupId 课程组名称
     * @param startDate       开始时间
     * @param endDate         结束时间
     * @return
     */
    @GetMapping("/teacher/table")
    public List<DataCensusDTO> queryHomeworkCompleteResult(@RequestParam(value = "lesson_group_id") Long lessonGroupId,
                                                           @RequestParam(value = "start_date") Long startDate,
                                                           @RequestParam(value = "end_date") Long endDate) {
        return dataCensusService.queryHomeworkCompleteResult(lessonGroupId, startDate, endDate);
    }

    /**
     * 查询某次作业全部学生的平均分，以折线图展示
     *
     * @param lessonGroupId 课程组id
     * @param startDate       开始时间
     * @param endDate         结束时间
     * @return
     */
    @GetMapping("/teacher/line")
    public List<DataCensusScoresDTO> queryAverageScore(@RequestParam(value = "lesson_group_id") Long lessonGroupId,
                                                       @RequestParam(value = "start_date") Long startDate,
                                                       @RequestParam(value = "end_date") Long endDate) {
        return dataCensusService.queryAverageScore(lessonGroupId, startDate, endDate);
    }

    /**
     * 查询所有(某次)作业的完成情况的百分比，老师视图
     *
     * @param lessonGroupId 课程组id
     * @param homeworkId    作业名称，可以为空，为空时查询所有作业；不为空时查询当前作业
     * @param startDate       开始时间
     * @param endDate         结束时间
     * @return 完成情况的百分比（优秀、良好、中等、及格、不及格、未完成）
     */
    @GetMapping("/teacher/pie")
    public List<Double> completePercent(@RequestParam(value = "lesson_group_id") Long lessonGroupId,
                                        @RequestParam(value = "homework_id", required = false) Long homeworkId,
                                        @RequestParam(value = "start_date") Long startDate,
                                        @RequestParam(value = "end_date") Long endDate) {
        return dataCensusService.teacherViewCompletePercent(lessonGroupId, homeworkId, startDate, endDate);
    }

    /**
     * 展示某学生所有课程或某个课程某个时间段内的得分情况，饼状图，学生视图
     *
     * @param lessonGroupId 课程组id
     * @param startDate       开始时间
     * @param endDate         结束时间
     * @return 完成情况的百分比（优秀、良好、中等、及格、不及格、未完成）
     */
    @GetMapping("/student/pie")
    public List<Double> completePercentStudent(@RequestParam(value = "lesson_group_id", required = false) Long lessonGroupId,
                                               @RequestParam(value = "start_date") Long startDate,
                                               @RequestParam(value = "end_date") Long endDate) {
        return dataCensusService.studentViewCompletePercent(lessonGroupId, startDate, endDate);
    }

    /**
     * 展示某学生某课程某时间段内的作业完成情况，折线图
     *
     * @param lessonGroupId 课程组名称
     * @param startDate       开始时间
     * @param endDate         结束时间
     * @return 作业和分数集合
     */
    @GetMapping("/student/line")
    public List<DataCensusScoresDTO> queryStudentScore(@RequestParam(value = "lesson_group_id", required = false) Long lessonGroupId,
                                                       @RequestParam(value = "start_date") Long startDate,
                                                       @RequestParam(value = "end_date") Long endDate) {
        return dataCensusService.queryStudentScore(lessonGroupId, startDate, endDate);
    }

}
