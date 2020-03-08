package com.h3c.vdi.athena.homework.service.homeworktemplate;

import com.h3c.vdi.athena.common.constant.BasicConstant;
import com.h3c.vdi.athena.common.exception.AppException;
import com.h3c.vdi.athena.common.model.DefaultUserDetails;
import com.h3c.vdi.athena.common.utils.Utils;
import com.h3c.vdi.athena.homework.constant.CommonConst;
import com.h3c.vdi.athena.homework.dao.HomeworkTemplateDao;
import com.h3c.vdi.athena.homework.dto.HomeworkDTO;
import com.h3c.vdi.athena.homework.dto.HomeworkTemplateDTO;
import com.h3c.vdi.athena.homework.dto.UserDTO;
import com.h3c.vdi.athena.homework.entity.Homework;
import com.h3c.vdi.athena.homework.entity.HomeworkTemplate;
import com.h3c.vdi.athena.homework.exception.ErrorCodes;
import com.h3c.vdi.athena.homework.feign.keystone.UserFeignService;
import com.h3c.vdi.athena.homework.service.attachment.AttachmentService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 作业模板业务类
 */
@Service("homeworkTemplateService")
public class HomeworkTemplateServiceImpl implements HomeworkTemplateService {

    @Resource
    private HomeworkTemplateDao homeworkTemplateDao;

    @Resource
    private UserFeignService userFeignService;

    @Resource
    private AttachmentService attachmentService;

    @Override
    public List<HomeworkTemplateDTO> queryAll() {
        List<HomeworkTemplate> homeworkTemplates = homeworkTemplateDao.findAll();
        return homeworkTemplates.stream().map(homeworkTemplate->this.convertToDTO(homeworkTemplate)).collect(Collectors.toList());
    }

    @Override
    public List<HomeworkTemplateDTO> queryAllByLoginUser() {
        DefaultUserDetails loginUser = Utils.getLoginUser();
        String username = loginUser.getUsername();
        UserDTO userDTO = userFeignService.findByUsername(username);
        List<HomeworkTemplate> homeworkTemplates = homeworkTemplateDao.findByUserId(userDTO.getId());
        return homeworkTemplates.stream().map(homeworkTemplate->this.convertToDTO(homeworkTemplate)).collect(Collectors.toList());
    }

    @Override
    public void addHomeworkTemplate(HomeworkTemplateDTO dto) {
        HomeworkTemplate byName = homeworkTemplateDao.findByNameAndIsDeleted(dto.getName(), BasicConstant.IS_DELETED_N);
        if (Objects.nonNull(byName)) {
            throw new AppException(ErrorCodes.HOMEWORK_TEMPLATE_NAME_REPEAT);
        }
        HomeworkTemplate homeworkTemplate = this.convertToEntity(dto);
        homeworkTemplate.setCreateTime(System.currentTimeMillis());
        homeworkTemplate.setIsDeleted(CommonConst.NOT_DELETED);
        Long homeworkTemplateID = homeworkTemplateDao.save(homeworkTemplate).getId();
        if(!CollectionUtils.isEmpty(dto.getAttachmentIds()))
            attachmentService.addAttachmentWithHomeworkTemplate(homeworkTemplateID,dto.getAttachmentIds());
    }

    @Override
    public void addHomeworkTemplateByHomeworkDTO(Homework homework,List<Long> attachmentIds){
        //作业模板名称不能重复
        List<String> homeworkTempNames = homeworkTemplateDao.findLikeName(homework.getName());
        if(!CollectionUtils.isEmpty(homeworkTempNames)&&homeworkTempNames.contains(homework.getName())){
            String homeworkTempName;
            for(int i=1;i<=homeworkTempNames.size();i++){
                homeworkTempName=homework.getName()+"("+i+")";
                if(!homeworkTempNames.contains(homeworkTempName)){
                    homework.setName(homeworkTempName);
                }
            }
        }
        HomeworkTemplate homeworkTemplate = new HomeworkTemplate();
        homeworkTemplate.setName(homework.getName());
        homeworkTemplate.setUserId(homework.getUserId());
        homeworkTemplate.setContent(homework.getContent());
        homeworkTemplate.setCreateTime(System.currentTimeMillis());
        homeworkTemplate.setIsDeleted(CommonConst.NOT_DELETED);
        HomeworkTemplate homeworkTemplateCreated=homeworkTemplateDao.save(homeworkTemplate);
        //将作业的附件复制一份并创建数据库记录并将其与作业模板关联
        attachmentService.addAttachmentWithHomeworkTemplate(homeworkTemplateCreated.getId(),attachmentIds);
    }

    @Override
    public HomeworkTemplateDTO queryHomeworkTemplateById(Long id) {
        HomeworkTemplate homeworkTemplate = homeworkTemplateDao.findByIdAndIsDeleted(id, BasicConstant.IS_DELETED_N);
        if (Objects.isNull(homeworkTemplate)) {
            throw new AppException(ErrorCodes.HOMEWORK_TEMPLATE_NOT_FOUND);
        }
        return this.convertToDTO(homeworkTemplate);
    }

    @Override
    public void modifyHomeworkTemplate(HomeworkTemplateDTO dto) {
        HomeworkTemplate db = homeworkTemplateDao.findByNameAndIsDeleted(dto.getName(), BasicConstant.IS_DELETED_N);
        if (Objects.nonNull(db) && !Objects.equals(db.getId(), dto.getId())) {
            throw new AppException(ErrorCodes.HOMEWORK_TEMPLATE_NAME_REPEAT);
        }
        HomeworkTemplate homeworkTemplate = homeworkTemplateDao.findByIdAndIsDeleted(dto.getId(),CommonConst.NOT_DELETED);
        homeworkTemplate.setName(dto.getName());
        homeworkTemplateDao.save(homeworkTemplate);
        this.modifyAttachmentOfHomeworkTemplate(dto);
    }

    //根据提交的homeworkTemplateDTO修改对应的附件信息
    private void modifyAttachmentOfHomeworkTemplate(HomeworkTemplateDTO dto){
        //检查附件是否有变化，如果有新添加的附件，将作业模板的ID添加到新加的附件记录中，如果有附件被从作业模板的附件列表中删除，删除记录和文件
        List<Long> attachmentIds=dto.getAttachmentIds();      //现在的附件IDlist
        //数据库中的关联到这个作业的附件IDlist
        List<Long> originalIds=attachmentService.getAttachmentIDsByTypeAndRelatedId(CommonConst.ATTACH_TYPE_TEMPLATE,dto.getId());
        //新添加的附件IDlist
        List<Long> toAddIds=attachmentIds.stream().filter(id->!originalIds.contains(id)).collect(Collectors.toList());
        //待删除的IDlist
        List<Long> toRemoveIds=originalIds.stream().filter(id->!attachmentIds.contains(id)).collect(Collectors.toList());
        if(!CollectionUtils.isEmpty(toAddIds))
            toAddIds.stream().forEach(id->attachmentService.setTypeAndRelatedId(id,CommonConst.ATTACH_TYPE_TEMPLATE,dto.getId()));
        if(!CollectionUtils.isEmpty(toRemoveIds))
            toRemoveIds.stream().forEach(id->attachmentService.deleteAttachmentById(id));
    }

    @Override
    public void deleteHomeworkTemplate(List<Long> ids) {
        List<HomeworkTemplate> homeworkTemplates = ids.stream().map(x -> homeworkTemplateDao.findOne(x)).collect(Collectors.toList());
        homeworkTemplates.forEach(x -> x.setIsDeleted(BasicConstant.IS_DELETED_Y));
        homeworkTemplateDao.save(homeworkTemplates);
        ids.forEach(id->//删除该作业下的附件
                attachmentService.deleteAttachmentsByTypeAndRelatedId(CommonConst.ATTACH_TYPE_TEMPLATE,id));
    }

    @Override
    public void deleteHomeworkTemplateByUserId(Long userId){
        List<HomeworkTemplate> homeworkTemplates = homeworkTemplateDao.findByUserId(userId);
        homeworkTemplates.forEach(homeworkTemplate -> homeworkTemplate.setIsDeleted(CommonConst.DELETED));
        homeworkTemplateDao.save(homeworkTemplates);
        homeworkTemplates.forEach(homeworkTemplate -> attachmentService.deleteAttachmentsByTypeAndRelatedId(CommonConst.ATTACH_TYPE_TEMPLATE,homeworkTemplate.getId()));
    }

    private HomeworkTemplateDTO convertToDTO(HomeworkTemplate homeworkTemplate) {
        HomeworkTemplateDTO dto = new HomeworkTemplateDTO();
        dto.setId(homeworkTemplate.getId());
        dto.setUserId(homeworkTemplate.getUserId());
        dto.setName(homeworkTemplate.getName());
        dto.setContent(homeworkTemplate.getContent());
        dto.setCreateTime(homeworkTemplate.getCreateTime());
        dto.setAttachmentDTOS(attachmentService.getAttachmentDTOsByTypeAndRelatedId(CommonConst.ATTACH_TYPE_TEMPLATE,homeworkTemplate.getId()));
        return dto;
    }

    private static HomeworkTemplate convertToEntity(HomeworkTemplateDTO dto) {
        HomeworkTemplate homeworkTemplate = new HomeworkTemplate();
        homeworkTemplate.setId(dto.getId());
        homeworkTemplate.setUserId(dto.getUserId());
        homeworkTemplate.setName(dto.getName());
        homeworkTemplate.setContent(dto.getContent());
        return homeworkTemplate;
    }


}
