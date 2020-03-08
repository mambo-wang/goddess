package com.h3c.vdi.athena.homework.controller;

import com.h3c.vdi.athena.common.exception.AppException;
import com.h3c.vdi.athena.common.model.CommonDTO;
import com.h3c.vdi.athena.common.utils.StringManager;
import com.h3c.vdi.athena.homework.dto.ClassEntityDTO;
import com.h3c.vdi.athena.homework.entity.ClassEntity;
import com.h3c.vdi.athena.homework.service.classEntity.ClassEntityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by z13339 on 2018/3/3.
 */
@RestController
@RequestMapping("/classEntity")
public class ClassEntityController {
    private Logger logger = LoggerFactory.getLogger(ClassEntityController.class);

    private static StringManager sm = StringManager.getManager(ClassEntity.class);

    @Resource
    private ClassEntityService classEntityService;

    /**
     * 查找所有班级
     *
     * @return 班级DTO
     */
    @RequestMapping(method = RequestMethod.GET)
    public List<ClassEntityDTO> findAllClasses() {
        List<ClassEntityDTO> classEntityDTOList = classEntityService.queryClassEntityDTOs();
        return classEntityDTOList;
    }

    /**
     * 根据学院Id查找所有班级
     *
     * @return 班级DTO
     */
    @RequestMapping(value = "/college/{collegeId}", method = RequestMethod.GET)
    public List<ClassEntityDTO> findAllClasses(@PathVariable(name = "collegeId") Long collegeId) {
        List<ClassEntityDTO> classEntityDTOList = classEntityService.queryClassEntitysByCollegeId(collegeId);
        return classEntityDTOList;
    }

    @RequestMapping(value = "/class/{classId}", method = RequestMethod.GET)
    public ClassEntityDTO findClassById(@PathVariable(name = "classId") Long classId){
        return classEntityService.queryClassEntityDTOById(classId);
    }
    /**
     * 添加班级
     *
     * @param classEntityDTO 班级DTO
     */
    @RequestMapping(method = RequestMethod.POST)
    public Integer addClassEntity(@RequestBody ClassEntityDTO classEntityDTO) {
        return classEntityService.addClassEntity(classEntityDTO);
    }

    /**
     * 修改班级
     *
     * @param classEntityDTO 班级DTO
     */
    @RequestMapping(method = RequestMethod.PUT)
    public void updateClassEntity(@RequestBody ClassEntityDTO classEntityDTO) {

        classEntityService.updateClassEntity(classEntityDTO);

    }

    /**
     * 删除班级
     *
     * @param commonDTO
     */
    @RequestMapping(value = "/delete", method = RequestMethod.PUT)
    public void deleteClassEntityBatch(@RequestBody CommonDTO<ArrayList<Long>> commonDTO) {
        ArrayList<Long> idList = commonDTO.getData();
        if (CollectionUtils.isEmpty(idList)) {
            return;
        }
        Long count = idList.stream()
                .filter(x -> classEntityService.deleteClassEntityByIds(x))
                .count();
        if (count == 0) {
            throw new AppException(sm.getString("delete.classEntity.failure"));
        } else if (count > 0 && count < idList.size()) {
            String resultDesc = sm.getString("delete.result", count, (idList.size() - count));
            throw new AppException(resultDesc);
        }
    }

    /**
     * 重置班级的邀请码
     * @param classId
     * @return
     */
    @RequestMapping(value = "/resetInviteCode/{classId}",method = RequestMethod.PUT)
    public Integer resetInviteCode(@PathVariable(name = "classId") Long classId){
        return classEntityService.resetInviteCode(classId);
    }


    @RequestMapping(value = "addbatch", method = RequestMethod.POST)
    public List<String> addClassEntityBatch(@RequestBody List<ClassEntityDTO> classEntityDTOList) {
        List<String> result = new ArrayList<>();
        try {
            if (CollectionUtils.isEmpty(classEntityDTOList)) {
                return result;
            }
            int listSize = classEntityDTOList.size();
            for (int i = 0; i < listSize; i++) {
                ClassEntityDTO entityDTO = classEntityDTOList.get(i);
                //String logMsg = sm.getString("classEntity.create", entityDTO.getName());
                try {
                    classEntityService.addClassEntity(classEntityDTOList.get(i));
                    // operationLogMgr.addSuccess(Categories.CLASS_ENTITY, logMsg);
                } catch (AppException e) {
                    result.add(i + ":" + entityDTO.getName() + "," + e.getErrorMessage());
                    //operationLogMgr.addFailure(Categories.CLASS_ENTITY, logMsg, e.getErrorMessage());
                    logger.warn("add ClassEntity failed", e);
                } catch (Exception e) {
                    result.add(i + ":" + entityDTO.getName() + "," + e.getMessage());
                    // operationLogMgr.addFailure(Categories.CLASS_ENTITY, logMsg, e.getMessage());
                    logger.warn("add ClassEntity failed", e);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return result;
    }


}
