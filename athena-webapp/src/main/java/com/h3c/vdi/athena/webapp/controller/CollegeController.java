package com.h3c.vdi.athena.webapp.controller;

import com.h3c.vdi.athena.common.model.CommonDTO;
import com.h3c.vdi.athena.webapp.dto.CollegeDTO;
import com.h3c.vdi.athena.webapp.service.HomeworkFeignService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by z13339 on 2018/3/3.
 */
@Api(value = "学院controller", tags = {"学院相关操作"})
@RestController
public class CollegeController {
    @Resource
    private HomeworkFeignService homeworkFeignService;

    //private static StringManager sm = StringManager.getManager(College.class);

    @ApiOperation(value = "查找学院",notes = "根据id查找学院")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header")
    @RequestMapping(value = "/homework/colleges/{id}", method = RequestMethod.GET)
    public CollegeDTO findById(@PathVariable(name = "id") Long id) {
        return homeworkFeignService.findCollegeById(id);
    }

    @ApiOperation(value = "添加学院",notes = "添加学院")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header")
    @RequestMapping(value = "/homework/colleges", method = RequestMethod.POST)
    public void addCollege(@RequestBody CollegeDTO collegeDTO) {
        homeworkFeignService.addCollege(collegeDTO);
    }

    @ApiOperation(value = "修改学院",notes = "修改学院")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header")
    @RequestMapping(value = "/homework/colleges", method = RequestMethod.PUT)
    public void updateCollege(@RequestBody CollegeDTO collegeDTO) {
        homeworkFeignService.updateCollege(collegeDTO);
    }

    @ApiOperation(value = "查询学院",notes = "查询所有学院")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header")
    @RequestMapping(value = "/homework/colleges",method = RequestMethod.GET)
    public List<CollegeDTO> queryAllColleges(){
       return homeworkFeignService.queryAllColleges();
    }

    @ApiOperation(value = "删除学院",notes = "批量删除学院")
    @ApiImplicitParam(name = "Authorization", value = "token", dataType = "String", paramType = "header")
    @RequestMapping(value = "/homework/colleges/{idList}", method = RequestMethod.DELETE)
    public void deleteCollege(@PathVariable ArrayList<Long> idList) {
        CommonDTO commonDTO = new CommonDTO();
        commonDTO.setData(idList);
        homeworkFeignService.deleteColleges(commonDTO);
    }


}
