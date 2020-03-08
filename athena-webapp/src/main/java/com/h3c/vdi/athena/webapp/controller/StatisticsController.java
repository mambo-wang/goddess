package com.h3c.vdi.athena.webapp.controller;

import com.h3c.vdi.athena.webapp.dto.DataCensusDTO;
import com.h3c.vdi.athena.webapp.dto.DataCensusScoresDTO;
import com.h3c.vdi.athena.webapp.service.DataCensusFeignService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * @author w14014
 * @date 2018/10/17
 */
@Api(value = "数据统计Controller", tags = {"数据统计操作接口"})
@Slf4j
@RestController
@RequestMapping(value = "/statistics")
public class StatisticsController {

    @Resource
    private DataCensusFeignService dataCensusFeignService;

    /**
     * 根据老师和其课程、时间段查询学生作业完成情况，以列表展示
     *
     * @param lessonGroupId 课程组名称
     * @param startDate       开始时间
     * @param endDate         结束时间
     * @return
     */
    @ApiOperation(value = "查询课程组成绩统计表",notes = "查询课程组内全部作业全部学生的成绩统计，按照平均分从高到低排序")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header")
    @GetMapping("/teacher/table")
    public List<DataCensusDTO> queryHomeworkCompleteResult(@ApiParam(value = "课程组id")@RequestParam(value = "lesson_group_id") Long lessonGroupId,
                                                           @ApiParam(value = "开始日期时间戳")@RequestParam(value = "start_date") Long startDate,
                                                           @ApiParam(value = "结束日期时间戳")@RequestParam(value = "end_date") Long endDate) {
        return dataCensusFeignService.queryHomeworkCompleteResult(lessonGroupId, startDate, endDate);
    }

    /**
     * 查询某次作业全部学生的平均分，以折线图展示
     *
     * @param lessonGroupId 课程组id
     * @param startDate       开始时间
     * @param endDate         结束时间
     * @return
     */
    @ApiOperation(value = "查询每次作业全部学生的平均分",notes = "查询某次作业全部学生的平均分，以折线图展示")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header")
    @GetMapping("/teacher/line")
    public List<DataCensusScoresDTO> queryAverageScore(@ApiParam(value = "课程组id")@RequestParam(value = "lesson_group_id") Long lessonGroupId,
                                                       @ApiParam(value = "开始日期时间戳")@RequestParam(value = "start_date") Long startDate,
                                                       @ApiParam(value = "结束日期时间戳")@RequestParam(value = "end_date") Long endDate) {
        return dataCensusFeignService.queryAverageScore(lessonGroupId, startDate, endDate);
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
    @ApiOperation(value = "查询所有(某次)作业的完成情况的百分比",notes = "查询所有(某次)作业的完成情况的百分比，老师视图")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header")
    @GetMapping("/teacher/pie")
    public List<Double> completePercent(@ApiParam(value = "课程组id")@RequestParam(value = "lesson_group_id") Long lessonGroupId,
                                        @ApiParam(value = "某次作业的id")@RequestParam(value = "homework_id", required = false) Long homeworkId,
                                        @ApiParam(value = "开始日期时间戳")@RequestParam(value = "start_date") Long startDate,
                                        @ApiParam(value = "结束日期时间戳")@RequestParam(value = "end_date") Long endDate) {
        return dataCensusFeignService.completePercent(lessonGroupId, homeworkId, startDate, endDate);
    }

    /**
     * 展示某学生所有课程或某个课程某个时间段内的得分情况，饼状图，学生视图
     *
     * @param lessonGroupId 课程组id
     * @param startDate       开始时间
     * @param endDate         结束时间
     * @return 完成情况的百分比（优秀、良好、中等、及格、不及格、未完成）
     */
    @ApiOperation(value = "展示某学生所有课程或某个课程某个时间段内的得分情况",notes = "展示某学生所有课程或某个课程某个时间段内的得分情况，饼状图，学生视图")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header")
    @GetMapping("/student/pie")
    public List<Double> completePercentStudent(@ApiParam(value = "课程组id")@RequestParam(value = "lesson_group_id", required = false) Long lessonGroupId,
                                               @ApiParam(value = "开始日期时间戳")@RequestParam(value = "start_date") Long startDate,
                                               @ApiParam(value = "结束日期时间戳")@RequestParam(value = "end_date") Long endDate) {
        return dataCensusFeignService.completePercentStudent(lessonGroupId, startDate, endDate);
    }

    /**
     * 展示某学生某课程某时间段内的作业完成情况，折线图
     *
     * @param lessonGroupId 课程组名称
     * @param startDate       开始时间
     * @param endDate         结束时间
     * @return 作业和分数集合
     */
    @ApiOperation(value = "展示某学生某课程某时间段内的作业完成情况",notes = "展示某学生某课程某时间段内的作业完成情况")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header")
    @GetMapping("/student/line")
    public List<DataCensusScoresDTO> queryStudentScore(@ApiParam(value = "课程组id")@RequestParam(value = "lesson_group_id", required = false) Long lessonGroupId,
                                                       @ApiParam(value = "开始日期时间戳")@RequestParam(value = "start_date") Long startDate,
                                                       @ApiParam(value = "结束日期时间戳")@RequestParam(value = "end_date") Long endDate) {
        return dataCensusFeignService.queryStudentScore(lessonGroupId, startDate, endDate);
    }

}
