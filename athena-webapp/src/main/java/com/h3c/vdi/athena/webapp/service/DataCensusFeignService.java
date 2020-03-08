package com.h3c.vdi.athena.webapp.service;

import com.h3c.vdi.athena.webapp.config.feign.FeignMultipartSupportConfig;
import com.h3c.vdi.athena.webapp.dto.DataCensusDTO;
import com.h3c.vdi.athena.webapp.dto.DataCensusScoresDTO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 *
 * @author w14014
 * @date 2018/10/17
 */
@FeignClient(value = "athena-gateway", configuration = FeignMultipartSupportConfig.class)
public interface DataCensusFeignService {

    @GetMapping("/homework/statistics/teacher/table")
    List<DataCensusDTO> queryHomeworkCompleteResult(@RequestParam(value = "lesson_group_id") Long lessonGroupId,
                                                           @RequestParam(value = "start_date") Long startDate,
                                                           @RequestParam(value = "end_date") Long endDate);


    @GetMapping("/homework/statistics/teacher/line")
    List<DataCensusScoresDTO> queryAverageScore(@RequestParam(value = "lesson_group_id") Long lessonGroupId,
                                                       @RequestParam(value = "start_date") Long startDate,
                                                       @RequestParam(value = "end_date") Long endDate);

    @GetMapping("/homework/statistics/teacher/pie")
    List<Double> completePercent(@RequestParam(value = "lesson_group_id") Long lessonGroupId,
                                        @RequestParam(value = "homework_id", required = false) Long homeworkId,
                                        @RequestParam(value = "start_date") Long startDate,
                                        @RequestParam(value = "end_date") Long endDate);

    @GetMapping("/homework/statistics/student/pie")
    List<Double> completePercentStudent(@RequestParam(value = "lesson_group_id", required = false) Long lessonGroupId,
                                               @RequestParam(value = "start_date") Long startDate,
                                               @RequestParam(value = "end_date") Long endDate);


    @GetMapping("/homework/statistics/student/line")
    List<DataCensusScoresDTO> queryStudentScore(@RequestParam(value = "lesson_group_id", required = false) Long lessonGroupId,
                                                       @RequestParam(value = "start_date") Long startDate,
                                                       @RequestParam(value = "end_date") Long endDate);

}
