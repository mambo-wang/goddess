package com.h3c.vdi.athena.homework.service.college;

import com.h3c.vdi.athena.common.constant.BasicConstant;
import com.h3c.vdi.athena.common.exception.AppException;
import com.h3c.vdi.athena.homework.dao.ClassEntityDao;
import com.h3c.vdi.athena.homework.dao.CollegeDao;
import com.h3c.vdi.athena.homework.dto.CollegeDTO;
import com.h3c.vdi.athena.homework.entity.ClassEntity;
import com.h3c.vdi.athena.homework.entity.College;
import com.h3c.vdi.athena.homework.exception.ErrorCodes;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by z13339 on 2018/3/2.
 */
@Service("collegeMgr")
public class CollegeServiceImpl implements CollegeService {

    @Resource
    private CollegeDao collegeDao;


    @Resource
    private ClassEntityDao classEntityDao;
    @Override
    public void addCollege(CollegeDTO collegeDTO) {
        this.validate(collegeDTO);
        College college = this.convertToCollege(collegeDTO);
        college.setDeleted(BasicConstant.IS_DELETED_N);
        collegeDao.save(college);
    }

    @Override
    public List<CollegeDTO> queryAllColleges(){
        List<College> colleges = collegeDao.findAll();
        if(!CollectionUtils.isEmpty(colleges)){
            return colleges.stream().map(college -> this.convertToCollegeDTO(college)).collect(Collectors.toList());
        }else
            return new ArrayList<>();
    }


    /**
     * 实体转换
     * @param collegeDTO
     * @return
     */
    private static College convertToCollege(CollegeDTO collegeDTO){
        College college=new College();
        college.setId(collegeDTO.getId());
        college.setName(collegeDTO.getName());
        return college;
    }

    /**
     * 验证学院参数是否合法
     * @param collegeDTO
     */
    private void validate(CollegeDTO collegeDTO) {
        if (null == collegeDTO) {
            throw new AppException(ErrorCodes.COLLEGE_NOT_COMPLETE);
        }
        String name = collegeDTO.getName();
        if (StringUtils.isEmpty(name) || StringUtils.containsWhitespace(name)) {
            throw new AppException(ErrorCodes.COLLEGE_NAME_ILLEGLE);
        }

        if (name.length() > 64) {
            throw new AppException(ErrorCodes.COLLEGE_NAME_LENGTH_MAX);
        }

        if (this.isNameExist(name, collegeDTO.getId())) {
            throw new AppException(ErrorCodes.COLLEGE_FOUND);
        }
    }

    private boolean isNameExist(String name, Long excludedId) {
        List<College> list = collegeDao.findByNameAndDeleted(name,BasicConstant.IS_DELETED_N);
        if (!CollectionUtils.isEmpty(list)) {
            for (College college : list) {
                if (!Objects.equals(college.getId(),excludedId)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean deleteCollege(Long id) {
        College college = collegeDao.findByIdAndDeleted(id,BasicConstant.IS_DELETED_N);
        if (null == college) {
            throw new AppException(ErrorCodes.COLLEGE_NOT_FOUND);
        }
        if (isCited(id)) {
            throw new AppException(ErrorCodes.COLLEGE_USED);
        }
        college.setDeleted(BasicConstant.IS_DELETED_Y);
        collegeDao.save(college);
        return true;
    }

    /**
     * 通过ID查询学院是否被引用
     * @param id
     * @return
     */
    private boolean isCited(Long id) {
        List<ClassEntity> list = classEntityDao.findByCollegeIdAndDeleted(id,BasicConstant.IS_DELETED_N);
        if (CollectionUtils.isEmpty(list)) {
            return false;
        }
        return true;
    }

    @Override
    public void updateCollege(CollegeDTO collegeDTO) {
        if (null == collegeDTO) {
            throw new AppException(ErrorCodes.COLLEGE_NOT_COMPLETE);
        }
        validate(collegeDTO);
        College college = this.convertToCollege(collegeDTO);
        college.setDeleted(BasicConstant.IS_DELETED_N);
        collegeDao.save(college);
    }

    @Override
    public CollegeDTO queryCollegeById(Long id) {
        College college = collegeDao.findOne(id);
        if (null == college) {
            throw new AppException(ErrorCodes.COLLEGE_NOT_FOUND);
        }
        CollegeDTO collegeDTO = this.convertToCollegeDTO(college);
        return collegeDTO;
    }

    /**
     *     College转换为CollegeDTO
     * @param college
     * @return
     */
    private static CollegeDTO convertToCollegeDTO(College college){
        CollegeDTO collegeDTO=new CollegeDTO();
        collegeDTO.setId(college.getId());
        collegeDTO.setName(college.getName());
        return collegeDTO;
    }

    @Override
    public College findCollegeById(Long id) {
        if(Objects.isNull(id)){
            return  null;
        }
        College byIdAndDeleted = collegeDao.findByIdAndDeleted(id, BasicConstant.IS_DELETED_N);
        return byIdAndDeleted;
    }
}
