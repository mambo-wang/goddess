package com.h3c.vdi.athena.homework.service.college;

import com.h3c.vdi.athena.homework.dto.CollegeDTO;
import com.h3c.vdi.athena.homework.entity.College;

import java.util.List;

/**
 * Created by z13339 on 2018/3/2.
 */
public interface CollegeService {

    /**
     * 添加一个学院
     * @param collegeDTO
     */
    void addCollege(CollegeDTO collegeDTO);

    List<CollegeDTO> queryAllColleges();

    /**
     * 删除一个学院
     * @param id
     */
    boolean deleteCollege(Long id);

    /**
     * 更新一个学院
     * @param collegeDTO
     */
    void updateCollege(CollegeDTO collegeDTO);
    /**
     * 根据ID查询学院
     * @param id
     * @return
     */
    CollegeDTO queryCollegeById(Long id);

    College findCollegeById(Long id);
}
