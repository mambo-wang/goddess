package com.h3c.vdi.athena.homework.controller;

import com.h3c.vdi.athena.homework.dto.HomeworkPraiseDTO;
import com.h3c.vdi.athena.homework.service.homeworkPraise.HomeworkPraiseService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by z13339 on 2018/4/24.
 */
@RestController
@RequestMapping("/homeworkPraise")
public class HomeworkPraiseController {
    @Resource
    private HomeworkPraiseService homeworkPraiseService;

    @RequestMapping(value = "/{homeworkSubmitId}/{userId}", method = RequestMethod.POST)
    public void addPraise(@PathVariable Long homeworkSubmitId,@PathVariable Long userId){
        homeworkPraiseService.addPraise(homeworkSubmitId,userId);
    }

    @RequestMapping(value = "/{homeworkPraiseId}/{userId}", method = RequestMethod.DELETE)
    public void deletePraise(@PathVariable Long homeworkPraiseId,@PathVariable Long userId){
        homeworkPraiseService.deletePraise(homeworkPraiseId,userId);
    }

    @RequestMapping(value = "/{homeworkPraiseId}", method = RequestMethod.GET)
    public List<HomeworkPraiseDTO> queryPraisesByHomeworkId(@PathVariable Long homeworkPraiseId){
        List<HomeworkPraiseDTO> homeworkPraiseDTOS = homeworkPraiseService.queryPraisesByHomeworkId(homeworkPraiseId);
        return homeworkPraiseDTOS;
    }
}
