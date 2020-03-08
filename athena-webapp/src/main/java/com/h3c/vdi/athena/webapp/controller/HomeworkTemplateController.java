package com.h3c.vdi.athena.webapp.controller;

import com.h3c.vdi.athena.common.model.CommonDTO;
import com.h3c.vdi.athena.webapp.dto.HomeworkTemplateDTO;
import com.h3c.vdi.athena.webapp.service.HomeworkFeignService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Api(value = "作业模板相关接口", tags = "作业模板相关操作接口")
@RestController
@RequestMapping("/homework/templates")
public class HomeworkTemplateController {

    @Resource
    private HomeworkFeignService homeworkFeignService;

    @ApiOperation(value = "查询所有作业模板",notes = "查询所有作业模板")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header")
    @GetMapping(value = "/all")
    public List<HomeworkTemplateDTO> queryAll() {
        return homeworkFeignService.queryAllTemplates();
    }

    @ApiOperation(value = "根据当前登录用户查询作业模板",notes = "根据当前登录用户查询所有作业模板")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header")
    @GetMapping
    public List<HomeworkTemplateDTO> queryByUser() {
        return homeworkFeignService.queryByLoginUser();
    }

    @ApiOperation(value = "根据id查询作业模板",notes = "根据id查询作业模板")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header")
    @GetMapping("/{id}")
    public HomeworkTemplateDTO queryHomeworkTemplateById(@PathVariable(value = "id") Long id) {
        return homeworkFeignService.queryTemplateById(id);
    }

    @ApiOperation(value = "新增作业模板",notes = "新增作业模板")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header")
    @PostMapping
    public List<HomeworkTemplateDTO> addHomeworkTemplate(@RequestBody HomeworkTemplateDTO dto) {
        return homeworkFeignService.addTemplate(dto);
    }

    @ApiOperation(value = "修改作业模板",notes = "修改作业模板")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header")
    @PutMapping
    public void modifyHomeworkTemplate(@RequestBody HomeworkTemplateDTO dto) {
        homeworkFeignService.modifyTemplate(dto);
    }

    @ApiOperation(value = "批量删除作业模板",notes = "批量删除作业模板")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header")
    @DeleteMapping("/{ids}")
    public void deleteHomeworkTemplate(@PathVariable(value = "ids") List<Long> ids) {
        CommonDTO commonDTO = new CommonDTO();
        commonDTO.setData(ids);
        homeworkFeignService.deleteTemplate(commonDTO);
    }
}
