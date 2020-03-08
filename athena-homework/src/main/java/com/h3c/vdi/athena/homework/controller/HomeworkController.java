package com.h3c.vdi.athena.homework.controller;

import com.h3c.vdi.athena.homework.dto.HomeworkDTO;
import com.h3c.vdi.athena.homework.dto.HomeworkSubDTO;
import com.h3c.vdi.athena.homework.dto.ShowHomeworkInfoDTO;
import com.h3c.vdi.athena.homework.entity.HomeworkSubmission;
import com.h3c.vdi.athena.homework.service.attachment.AttachmentService;
import com.h3c.vdi.athena.homework.service.homework.HomeworkService;
import com.h3c.vdi.athena.homework.service.homeworkSubmission.HomeworkSubmissionService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * Created by w16051 on 2018/3/26.
 */
@RestController
@RequestMapping(value = "/homeworks")
public class HomeworkController {

    @Resource
    private HomeworkService homeworkService;

    @Resource
    private HomeworkSubmissionService homeworkSubmissionService;

    /**
     * 查询当前登录人发布的作业
     * @return
     */
    @GetMapping
    public List<HomeworkDTO> queryHomeworkDTOs()
    {
        return homeworkService.queryHomeworkDTOs();
    }

    @GetMapping(value = "/{id}")
    public HomeworkDTO queryHomeworkDTO(@PathVariable Long id)
    {
        return homeworkService.queryHomeworkDTOById(id);
    }

    /**
     * 发布作业
     * @param homeworkDTO
     */
    @PostMapping
    public void addHomework(@RequestBody HomeworkDTO homeworkDTO){
        homeworkService.addHomework(homeworkDTO);
    }

    /**
     * 修改作业
     * @param homeworkDTO
     */
    @PutMapping
    public void updateHomework(@RequestBody HomeworkDTO homeworkDTO){
        homeworkService.modifyHomework(homeworkDTO);
    }

    /**
     * todo 异步调用还没写
     * 删除作业，连带删除该作业下学生提交的作业和附件
     * @param id
     */
    @PutMapping(value = "/{id}")
    public void deleteHomework(@PathVariable Long id){
        homeworkService.deleteHomework(id);
    }

    /**
     * 查询某作业的所有学生作业提交的状况
     * @param id
     * @return
     */
    @GetMapping(value = "/{id}/submitted_works")
    public List<HomeworkSubDTO> queryAllHomeworkSubmissions(@PathVariable Long id){
        return homeworkSubmissionService.queryHomeworkSubDTOByHomeworkId(id);
    }

    /**
     * 修改是否展示
     * @param showHomeworkInfoDTO
     */
    @PutMapping(value = "/submitted_works/showed")
    public void changeShowStatus(@RequestBody ShowHomeworkInfoDTO showHomeworkInfoDTO){
        homeworkSubmissionService.changeShowStatus(showHomeworkInfoDTO);
    }

    /**
     * 获取某学生提交作业详情给老师批改
     * @param id
     * @return
     */
    @GetMapping(value = "/submitted_works/{id}")
    public HomeworkSubDTO getHomeworkSubDTO(@PathVariable Long id){
        return homeworkSubmissionService.queryHomeworkSubDTOById(id);
    }


    /**
     * 给作业打分
     * @param homeworkSubDTO
     */
    @PutMapping(value = "/submitted_works")
    public void setScore(@RequestBody HomeworkSubDTO homeworkSubDTO){
        homeworkSubmissionService.setScore(homeworkSubDTO);
    }

    /**
     * 查询某教师作业被展示的学生作业
     * @param homeworkId
     * @return
     */
    @GetMapping(value = "/showed_homeworks/{homeworkId}")
    public List<HomeworkSubDTO> queryShowedHomeworkDTOs(@PathVariable Long homeworkId){
        return homeworkSubmissionService.queryShowedHomeworkSubDTOs(homeworkId);
    }

    /**
     * 查询某课程组下面所有教师作业
     * @param groupId
     * @return
     */
    @GetMapping(value = "/lessonGroup/{groupId}")
    public List<HomeworkDTO> queryHomeworkDTOsByLessonGroupId(@PathVariable Long groupId){
        return homeworkService.queryHomeworkDTOsByGroupId(groupId);
    }

    /**
     * 根据课程组id查询该课程组下面被展示的作业
     * @param id
     * @return
     */
    @GetMapping(value = "/showed_homeworks/lessonGroup/{id}")
    public List<HomeworkSubDTO> queryShowedHomeworkDTOsByGroupId(@PathVariable Long id){
        return homeworkSubmissionService.queryShowedHomeworkSubDTOsByGroupId(id);
    }
}
