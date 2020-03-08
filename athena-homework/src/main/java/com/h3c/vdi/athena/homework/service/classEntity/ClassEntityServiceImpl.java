package com.h3c.vdi.athena.homework.service.classEntity;

import com.h3c.vdi.athena.common.constant.BasicConstant;
import com.h3c.vdi.athena.common.exception.AppException;
import com.h3c.vdi.athena.common.model.CommonDTO;
import com.h3c.vdi.athena.homework.constant.CommonConst;
import com.h3c.vdi.athena.homework.dao.ClassEntityDao;
import com.h3c.vdi.athena.homework.dao.CollegeDao;
import com.h3c.vdi.athena.homework.dao.UserClassRelationDao;
import com.h3c.vdi.athena.homework.dto.ClassEntityDTO;
import com.h3c.vdi.athena.homework.dto.CollegeDTO;
import com.h3c.vdi.athena.homework.dto.CommentDTO;
import com.h3c.vdi.athena.homework.dto.UserDTO;
import com.h3c.vdi.athena.homework.entity.ClassEntity;
import com.h3c.vdi.athena.homework.entity.College;
import com.h3c.vdi.athena.homework.entity.UserClassRelation;
import com.h3c.vdi.athena.homework.exception.ErrorCodes;
import com.h3c.vdi.athena.homework.feign.keystone.UserFeignService;
import com.h3c.vdi.athena.homework.service.college.CollegeService;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.util.SystemPropertyUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
/**
 * Created by z13339 on 2018/3/2.
 */
@Service
public class ClassEntityServiceImpl implements ClassEntityService {


    @Resource
    private ClassEntityDao classEntityDao;

    @Resource
    private CollegeService collegeMgr;

    @Resource
    private UserClassRelationDao userClassRelationDao;

    @Resource
    private UserFeignService userFeignService;

    @Override
    public Integer addClassEntity(ClassEntityDTO classEntityDTO) {
        if (null == classEntityDTO) {
            throw new AppException(ErrorCodes.CLASS_ENTITY_NOT_COMPLETE);
        }
        this.validateClassEntityDTO(classEntityDTO);
        Integer inviteCode = this.buildInviteCode();
        ClassEntity classEntity = new ClassEntity();
        classEntity = this.toClassEntity(classEntity,classEntityDTO);
        classEntity.setDeleted(BasicConstant.IS_DELETED_N);
        classEntity.setInviteCode(inviteCode);
        classEntity.setCreateTime(new Date(System.currentTimeMillis()));
        classEntityDao.save(classEntity);
        return inviteCode;
    }

    //生成4位邀请码，并且将该邀请码和已有邀请码进行比较，如果和已有邀请码重复了，则重新生成随机数，最多尝试5次，如果第五次仍然重复就认命吧，重复了也没太大的问题
    private Integer buildInviteCode(){
        Integer code = (int)((Math.random()*9+1)*1000);
        List<Integer> codes = classEntityDao.queryInviteCodes();
        if(!CollectionUtils.isEmpty(codes)&&codes.contains(code)){
            for(int i =0;i<5;i++){
                code = (int)((Math.random()*9+1)*1000);
                if(!codes.contains(code))
                    return code;
            }
        }
        return code;
    }

    private  ClassEntity toClassEntity(ClassEntity classEntity,ClassEntityDTO classEntityDTO) {
        classEntity.setName(classEntityDTO.getName());
        classEntity.setCode(classEntityDTO.getCode());
        classEntity.setCreateTime(classEntityDTO.getCreateTime());
        classEntity.setMonitorId(classEntityDTO.getMonitorId());
        classEntity.setCollegeId(classEntityDTO.getCollegeId());
        return classEntity;
    }

    /**
     * 校验班级实体，包含属性基本校验，属性重复性校验。
     *
     * @param classEntityDTO
     */
    private void validateClassEntityDTO(ClassEntityDTO classEntityDTO) {
        this.checkItems(classEntityDTO);
        if (isCodeExist(classEntityDTO.getName())) {
            throw new AppException(ErrorCodes.CLASS_ENTITY_CODE_EXISTED);
        }
    }

    /**
     * 根据班级号查询班级实体是否存在，可以通过excludedId排除掉指定行。如果无需排除可以设置为-1。
     *
     * @param code
     * @return
     */
    private boolean isCodeExist(String code) {
        List<ClassEntity> list = classEntityDao.findByCodeAndDeleted(code, BasicConstant.IS_DELETED_N);
        if (!CollectionUtils.isEmpty(list)) {
            return true;
        }
        return false;
    }

    /**
     * 班级DTO的属性校验
     *
     * @param classEntityDTO
     */
    private void checkItems(ClassEntityDTO classEntityDTO) {
        String name = classEntityDTO.getName();
        Long clolegeId = classEntityDTO.getCollegeId();
        if (StringUtils.isEmpty(name)) {
            throw new AppException(ErrorCodes.CLASS_NAME_NOT_EMPTY);
        }

        if (name.length() > 64) {
            throw new AppException(ErrorCodes.ClASS_NAME_LENGTH_MAX);
        }
        if (Objects.isNull(clolegeId)) {
            throw new AppException(ErrorCodes.CLASS_COLLEGEID_NOT_EMPTY);
        }
    }

    @Override
    public void updateClassEntity(ClassEntityDTO classEntityDTO) {
        if (null == classEntityDTO || null == classEntityDTO.getId()) {
            throw new AppException(ErrorCodes.CLASS_ENTITY_NOT_COMPLETE);
        }
        checkItems(classEntityDTO);
        ClassEntity classEntity = this.queryClassEntityById(classEntityDTO.getId());
        if(Objects.isNull(classEntity)){
            throw new AppException("班级已被删除，请刷新后重试");
        }
        classEntity = this.toClassEntity(classEntity, classEntityDTO);
        classEntityDao.save(classEntity);
    }

    @Override
    public ClassEntity queryClassEntityById(Long id) {
        ClassEntity classEntity = classEntityDao.findByIdAndDeleted(id,BasicConstant.IS_DELETED_N);
        return classEntity;
    }

    @Override
    public boolean deleteClassEntityByIds(Long id) {
        ClassEntity one = classEntityDao.findByIdAndDeleted(id,BasicConstant.IS_DELETED_N);
        if (null == one) {
            throw new AppException(ErrorCodes.CLASS_ENTITY_NOT_FOUND);
        }
        List<UserClassRelation> userClassRelations = userClassRelationDao.findByClassId(id);
        if(!CollectionUtils.isEmpty(userClassRelations)){
            throw new AppException("班级被引用，不能删除，请先删除班级下的学生");
        }
        one.setDeleted(BasicConstant.IS_DELETED_Y);
        classEntityDao.save(one);
        return true;
    }

    @Override
    public List<ClassEntityDTO> queryClassEntityDTOs() {
        List<ClassEntity> allClassesExceptDefaultClass = classEntityDao.findAll().stream()
                .collect(Collectors.toList());
        return this.toClassEntityDTOs(allClassesExceptDefaultClass);
    }

    private List<ClassEntityDTO> toClassEntityDTOs(List<ClassEntity> classEntityList) {

        return classEntityList.stream()
                .map(a -> toClassEntityDTO(a))
                .collect(Collectors.toList());
    }

    private ClassEntityDTO toClassEntityDTO(ClassEntity classEntity) {
        ClassEntityDTO classEntityDTO = new ClassEntityDTO();
        BeanUtils.copyProperties(classEntity, classEntityDTO);
        if(Objects.nonNull(classEntity.getMonitorId())){
            UserDTO userDTO=userFeignService.queryUserById(classEntity.getMonitorId());
            if(Objects.nonNull(userDTO))
                classEntityDTO.setMonitorName(userDTO.getName());
        }
        if(Objects.nonNull(classEntity.getCollegeId())){
            CollegeDTO collegeDTO = collegeMgr.queryCollegeById(classEntity.getCollegeId());
            if(Objects.nonNull(collegeDTO))
                classEntityDTO.setCollegeName(collegeDTO.getName());
        }
        return classEntityDTO;
    }

    @Override
    public ClassEntityDTO queryClassEntityDTOById(Long id) {
        ClassEntity classEntity = queryClassEntityById(id);
        ClassEntityDTO classEntityDTO = this.toClassEntityDTO(classEntity);
        College college = collegeMgr.findCollegeById(classEntity.getCollegeId());
        classEntityDTO.setCollegeName(college.getName());
        if(Objects.nonNull(classEntityDTO.getMonitorId())){
            UserDTO monitor = userFeignService.queryUserById(classEntity.getMonitorId());
            if (Objects.nonNull(monitor))
                classEntityDTO.setMonitorName(monitor.getName());
        }
//        if (classEntityDTO.getName().equals(DEFAULT_CLASS_NAME)) {
//            classEntityDTO.setEntranceYear(null);
//        }
//        if (needStuInfo) {
//            this.addUserInfoToDTO(classEntityDTO);
//        }
        return classEntityDTO;
    }

    @Override
    public List<ClassEntityDTO> queryClassEntitysByCollegeId(Long collegeId) {
        if(Objects.isNull(collegeId)) {
            return null;
        }
        List<ClassEntity> classEntities = classEntityDao.findByCollegeIdAndDeleted(collegeId, BasicConstant.IS_DELETED_N);
        List<ClassEntityDTO> classEntityDTOS = this.toClassEntityDTOs(classEntities);
        return classEntityDTOS;
    }

    @Override
    public Integer resetInviteCode(Long classId){
        ClassEntity classEntity = classEntityDao.findByIdAndDeleted(classId, CommonConst.NOT_DELETED);
        if(Objects.isNull(classEntity))
            throw new AppException(ErrorCodes.CLASS_ENTITY_NOT_FOUND);
        Integer code = this.buildInviteCode();
        classEntity.setInviteCode(code);
        classEntityDao.save(classEntity);
        return code;
    }
}
