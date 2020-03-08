package com.h3c.vdi.athena.webapp.controller;
import com.h3c.vdi.athena.webapp.dto.HomeworkPraiseDTO;
import com.h3c.vdi.athena.webapp.service.HomeworkFeignService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by z13339 on 2018/4/24.
 */
@Api(value = "点赞controller", tags = {"点赞相关操作"})
@RestController
public class HomeworkPraiseController {
    @Resource
    private HomeworkFeignService homeworkFeignService;


    @ApiOperation(value = "点赞",notes = "为提交的作业点赞")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header")
    @RequestMapping(value = "/homework/homeworkPraise/{homeworkSubmitId}/{userId}", method = RequestMethod.POST)
    public void addPraise(@PathVariable Long homeworkSubmitId,@PathVariable Long userId) {
        homeworkFeignService.addPraise(homeworkSubmitId,userId);
    }


    @ApiOperation(value = "删除点赞",notes = "删除点赞")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header")
    @RequestMapping(value = "/homework/homeworkPraise/{homeworkPraiseId}/{userId}", method = RequestMethod.DELETE)
    public void deletePraise(@PathVariable Long homeworkPraiseId,@PathVariable Long userId) {
        homeworkFeignService.deletePraise(homeworkPraiseId,userId);
    }


    @ApiOperation(value = "查询点赞记录",notes = "查询点赞记录")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header")
    @RequestMapping(value = "/homework/homeworkPraise/{homeworkPraiseId}", method = RequestMethod.GET)
    public List<HomeworkPraiseDTO> queryPraisesByHomeworkId(@PathVariable Long homeworkPraiseId) {
        return homeworkFeignService.queryPraisesByHomeworkId(homeworkPraiseId);
    }

}
