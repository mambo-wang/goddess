package com.h3c.vdi.athena.homework.controller;

import com.h3c.vdi.athena.homework.dto.HomeworkDTO;
import com.h3c.vdi.athena.homework.dto.HomeworkSubDTO;
import com.h3c.vdi.athena.homework.service.homework.HomeworkService;
import com.h3c.vdi.athena.homework.service.homeworkSubmission.HomeworkSubmissionService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by w16051 on 2018/4/9.
 */
@RestController
@RequestMapping(value = "/my/homeworks")
public class HomeworkStuController {

    @Resource
    private HomeworkSubmissionService homeworkSubmissionService;

    @Resource
    private HomeworkService homeworkService;

    /**
     * 查询当前登录学生的所有作业
     * @return
     */
    @GetMapping()
    public List<HomeworkDTO> queryAllHomeworkSubDTOs(){
        return homeworkService.queryHomeworkDTOs();
    }

    /**
     * 查看作业详情，包含作业题目和已提交的内容
     * @param homeworkId
     * @return
     */
    @GetMapping(value = "/{homeworkId}")
    public HomeworkSubDTO queryHomeworkSubDTOByHomeworkId(@PathVariable Long homeworkId){
        return homeworkSubmissionService.queryByHomeworkIdAndCurrentUser(homeworkId);
    }

    /**
     * 学生提交作业
     * @param homeworkSubDTO
     */
    @PostMapping()
    public void submitHomework(@RequestBody HomeworkSubDTO homeworkSubDTO){
        homeworkSubmissionService.submitHomework(homeworkSubDTO);
    }

    /**
     * 学生修改作业
     * @param homeworkSubDTO
     */
    @PutMapping()
    public void updateHomeworkSubmission(@RequestBody HomeworkSubDTO homeworkSubDTO){
        homeworkSubmissionService.updateHomeworkSubmission(homeworkSubDTO);
    }
}
