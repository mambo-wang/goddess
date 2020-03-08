package com.h3c.vdi.athena.homework.service.classEntity;

import com.h3c.vdi.athena.homework.dto.ClassEntityDTO;
import com.h3c.vdi.athena.homework.entity.ClassEntity;

import java.util.List;

/**
 * Created by z13339 on 2018/3/2.
 */
public interface ClassEntityService {

    /**
     * 查询所有班级信息
     * @return
     */
    List<ClassEntityDTO> queryClassEntityDTOs();

    /**
     * collegeId查询所有班级信息
     * @param collegeId
     * @return
     */
    List<ClassEntityDTO> queryClassEntitysByCollegeId(Long collegeId);
    /**
     * 添加一条班级信息
     * @param classEntityDTO
     */
    Integer addClassEntity(ClassEntityDTO classEntityDTO);

    /**
     * 更新班级信息
     * @param classEntityDTO
     */
    void updateClassEntity(ClassEntityDTO classEntityDTO);

    /**
     * 通过班级ID查询班级实体
     * @param id 班级id
     * @return
     */
    ClassEntityDTO queryClassEntityDTOById(Long id);

    /**
     * 通过班级ID查询班级实体
     * @param id
     * @return
     */
    ClassEntity queryClassEntityById(Long id);

    /**
     * 删除一个班级实体
     * @param id
     */
    boolean deleteClassEntityByIds(Long id);

    /**
     * 重新生成邀请码
     * @param classId
     * @return
     */
    Integer resetInviteCode(Long classId);
}
