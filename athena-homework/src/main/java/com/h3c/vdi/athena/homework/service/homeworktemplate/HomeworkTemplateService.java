package com.h3c.vdi.athena.homework.service.homeworktemplate;

import com.h3c.vdi.athena.homework.dto.HomeworkDTO;
import com.h3c.vdi.athena.homework.dto.HomeworkTemplateDTO;
import com.h3c.vdi.athena.homework.entity.Homework;

import java.util.List;

/**
 * 作业模板
 */
public interface HomeworkTemplateService {

    List<HomeworkTemplateDTO> queryAll();

    List<HomeworkTemplateDTO> queryAllByLoginUser();

    void addHomeworkTemplate(HomeworkTemplateDTO homeworkTemplateDTO);

    /**
     * 根据作业创建作业模板
     * @param homework
     * @param attachmentIds
     */
    void addHomeworkTemplateByHomeworkDTO(Homework homework,List<Long> attachmentIds);

    HomeworkTemplateDTO queryHomeworkTemplateById(Long id);

    void modifyHomeworkTemplate(HomeworkTemplateDTO homeworkTemplateDTO);

    void deleteHomeworkTemplate(List<Long> ids);

    void deleteHomeworkTemplateByUserId(Long userId);
}
