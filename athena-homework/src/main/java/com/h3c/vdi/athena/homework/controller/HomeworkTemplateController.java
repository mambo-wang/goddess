package com.h3c.vdi.athena.homework.controller;

import com.h3c.vdi.athena.common.model.CommonDTO;
import com.h3c.vdi.athena.homework.dto.HomeworkTemplateDTO;
import com.h3c.vdi.athena.homework.service.homeworktemplate.HomeworkTemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 作业模板控制器
 */
@RestController
@RequestMapping("/templates")
public class HomeworkTemplateController {

    @Resource
    private HomeworkTemplateService homeworkTemplateService;

    @GetMapping("/all")
    public List<HomeworkTemplateDTO> queryAll() {
        return homeworkTemplateService.queryAll();
    }

    @GetMapping
    public List<HomeworkTemplateDTO> queryAllByLoginUser() {
        return homeworkTemplateService.queryAllByLoginUser();
    }

    @GetMapping("/{id}")
    public HomeworkTemplateDTO queryHomeworkTemplateById(@PathVariable(value = "id") Long id) {
        return homeworkTemplateService.queryHomeworkTemplateById(id);
    }

    @PostMapping("/templates")
    public List<HomeworkTemplateDTO> addHomeworkTemplate(@RequestBody HomeworkTemplateDTO dto) {
        homeworkTemplateService.addHomeworkTemplate(dto);
        return homeworkTemplateService.queryAll();
    }

    @PutMapping
    public void modifyHomeworkTemplate(@RequestBody HomeworkTemplateDTO dto) {
        homeworkTemplateService.modifyHomeworkTemplate(dto);
    }

    @PutMapping("/delete")
    public void deleteHomeworkTemplate(@RequestBody CommonDTO<ArrayList<Long>> commonDTO) {
        ArrayList<Long> ids = commonDTO.getData();
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }
        homeworkTemplateService.deleteHomeworkTemplate(ids);
    }
}
