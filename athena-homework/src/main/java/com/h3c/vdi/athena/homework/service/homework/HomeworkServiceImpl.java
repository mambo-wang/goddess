package com.h3c.vdi.athena.homework.service.homework;

import com.h3c.vdi.athena.common.constant.BasicConstant;
import com.h3c.vdi.athena.common.exception.AppException;
import com.h3c.vdi.athena.homework.constant.CommonConst;
import com.h3c.vdi.athena.homework.dao.HomeworkDao;
import com.h3c.vdi.athena.homework.dao.HomeworkSubmissionDao;
import com.h3c.vdi.athena.homework.dto.HomeworkDTO;
import com.h3c.vdi.athena.homework.dto.RoleType;
import com.h3c.vdi.athena.homework.dto.UserDTO;
import com.h3c.vdi.athena.homework.entity.Homework;
import com.h3c.vdi.athena.homework.entity.HomeworkSubmission;
import com.h3c.vdi.athena.homework.entity.LessonGroup;
import com.h3c.vdi.athena.homework.exception.ErrorCodes;
import com.h3c.vdi.athena.homework.service.attachment.AttachmentService;
import com.h3c.vdi.athena.homework.service.homeworkSubmission.HomeworkSubmissionService;
import com.h3c.vdi.athena.homework.service.homeworktemplate.HomeworkTemplateService;
import com.h3c.vdi.athena.homework.service.lessongroup.LessonGroupService;
import com.h3c.vdi.athena.homework.service.user.UserService;
import io.jsonwebtoken.lang.Collections;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by w16051 on 2018/3/21.
 */
@Service
public class HomeworkServiceImpl implements HomeworkService{

    @Resource
    HomeworkSubmissionDao homeworkSubmissionDao;

    @Resource
    HomeworkDao homeworkDao;

    @Resource
    LessonGroupService lessonGroupService;

    @Resource
    AttachmentService attachmentService;

    @Resource
    UserService userService;

    @Resource
    HomeworkSubmissionService homeworkSubmissionService;

    @Resource
    HomeworkTemplateService homeworkTemplateService;

    @Override
    public List<HomeworkDTO> queryHomeworkDTOs()
    {
        UserDTO currentLoginUser=userService.currentLoginUser();
        String roleName = userService.getRoleName(currentLoginUser);
        List<HomeworkDTO> homeworkDTOS=new ArrayList<>();
        //如果是老师，查出来他下发的作业
        if(StringUtils.equals(roleName, RoleType.TEACHER.getName()) ||
                StringUtils.equals(roleName, RoleType.ADMIN.getName())){
            List<Homework> homeworks=homeworkDao.queryHomeworkByUserId(currentLoginUser.getId());
            homeworkDTOS=homeworks.stream()
                    .map(homework->convertFromHomeworkToHomeworkDTO(homework,false))
                    .collect(Collectors.toList());
        }
        //如果是学生，先查出他所属的课程组，然后根据课程组查出他要做的作业
        else {
            List<LessonGroup> groups=lessonGroupService.queryGroupByCurrentLoginUser();
            List<Homework> homeworks=groups.stream()
                    .map(group->homeworkDao.queryByGroupId(group.getId()))
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());
            homeworkDTOS=homeworks.stream().map(h->convertFromHomeworkToHomeworkDTO(h,false))
                    .collect(Collectors.toList());
        }
        return homeworkDTOS;
    }

    private HomeworkDTO convertFromHomeworkToHomeworkDTO(Homework homework,Boolean ifWithAttach)
    {
        HomeworkDTO homeworkDTO=new HomeworkDTO();
        BeanUtils.copyProperties(homework,homeworkDTO);
        //设置作业所属的课程组DTO
        homeworkDTO.setLessonGroupDTO(lessonGroupService.convertGroupToDTO(homework.getLessonGroup()));
        List<HomeworkSubmission> homeworkSubmissions=homeworkSubmissionDao.queryHomeworkSubmissionByHomeworkId(homework.getId());
        //设置作业已提交数
        homeworkDTO.setSubmittedNum(homeworkSubmissions.size());
        List<HomeworkSubmission> homeworkSubmissionsWithScore=homeworkSubmissions.stream()
                .filter(homeworkSubmission -> Objects.nonNull(homeworkSubmission.getScore()))
                .collect(Collectors.toList());
        //设置已评分数
        homeworkDTO.setScoredNum(homeworkSubmissionsWithScore.size());
        //设置已评分的平均分
        homeworkDTO.setAverageScore(getAverageScore(homeworkSubmissionsWithScore));
        //设置附件
        if(ifWithAttach)
            homeworkDTO.setAttachmentDTOS(attachmentService.getAttachmentDTOsByTypeAndRelatedId(CommonConst.ATTACH_TYPE_HOMEWORK,homework.getId()));
        return homeworkDTO;
    }

    private Integer getAverageScore(List<HomeworkSubmission> homeworkSubmissions)
    {
        List<Integer> scores =homeworkSubmissions.stream()
                .map(m->m.getScore()).collect(Collectors.toList());
        Integer scoreSize = scores.size();
        if(scoreSize.equals(0)){
            return 0;
        }
        Integer scoreSum=0;
        for (Integer score: scores) {
            scoreSum+=score;
        }
        return Math.round(scoreSum/scoreSize);
    }

    @Override
    public HomeworkDTO queryHomeworkDTOById(Long Id)
    {
        Homework homework=homeworkDao.findOne(Id);
        return convertFromHomeworkToHomeworkDTO(homework,true);
    }

    @Override
    public void addHomework(HomeworkDTO homeworkDTO){
        Homework homework=new Homework();
        Long userId=userService.currentLoginUser().getId();
        judgeHomeworkDTO(homeworkDTO,userId);
        homework.setUserId(userId);
        LessonGroup lessonGroup=lessonGroupService.findGroupById(homeworkDTO.getLessonGroupDTO().getId());
        homework.setLessonGroup(lessonGroup);
        homework.setName(homeworkDTO.getName());
        homework.setContent(homeworkDTO.getContent());
        homework.setCreateTime(System.currentTimeMillis());
        homework.setDeadLine(homeworkDTO.getDeadLine());
        homework.setSaved(homeworkDTO.getIsSaved());
        homework.setDeleted(CommonConst.NOT_DELETED);
        Homework homeworkSaved=homeworkDao.save(homework);
        //处理附件，将已上传的附件关联到新建的作业记录
        List<Long> attachmentIds = homeworkDTO.getAttachmentIds();
        if(!CollectionUtils.isEmpty(homeworkDTO.getAttachmentUrls()))
            attachmentIds.addAll(attachmentService.makeAttachmentsByUrls(homeworkDTO.getAttachmentUrls()));
        if(!CollectionUtils.isEmpty(attachmentIds))
            attachmentIds.stream().forEach(id->attachmentService.setTypeAndRelatedId(id,CommonConst.ATTACH_TYPE_HOMEWORK,homeworkSaved.getId()));
        if(homeworkDTO.getIsSaved().equals("y")){
            homeworkTemplateService.addHomeworkTemplateByHomeworkDTO(homework,attachmentIds);
        }
    }

    /**
     * 审核作业DTO的合法性
     * @param homeworkDTO
     */
    private void judgeHomeworkDTO(HomeworkDTO homeworkDTO,Long userId){
        if(Objects.isNull(homeworkDTO.getLessonGroupDTO())||Objects.isNull(homeworkDTO.getLessonGroupDTO().getId())
                ||Objects.isNull(homeworkDTO.getName()))
            throw new AppException(ErrorCodes.HOMEWORK_ILLEGAL);
        Homework homework=homeworkDao.queryByNameAndGroupIdAndUserId(homeworkDTO.getName(),homeworkDTO.getLessonGroupDTO().getId(),userId);
        if(!Objects.isNull(homework)){
            if(Objects.isNull(homeworkDTO.getId())||(Objects.nonNull(homeworkDTO.getId())&&!Objects.equals(homework.getId(),homeworkDTO.getId())))
                throw new AppException(ErrorCodes.HOMEWORK_NAME_DUPLICATED);
        }
    }

    @Override
    public List<Homework> queryHomeworkByLessonGroupAndDate(Long groupId, Long startDate, Long endDate) {
        return homeworkDao.queryHomework(groupId, startDate, endDate);
    }

    @Override
    public Homework queryHomeworkByName(String name) {
        return homeworkDao.queryByNameAndDeleted(name, BasicConstant.IS_DELETED_N);
    }

    @Override
    public void modifyHomework(HomeworkDTO homeworkDTO){
        Long userId=userService.currentLoginUser().getId();
        judgeHomeworkDTO(homeworkDTO,userId);
        Homework homework=homeworkDao.findOne(homeworkDTO.getId());
        homework.setLessonGroup(lessonGroupService.findGroupById(homeworkDTO.getLessonGroupDTO().getId()));
        homework.setName(homeworkDTO.getName());
        homework.setContent(homeworkDTO.getContent());
        homework.setDeadLine(homeworkDTO.getDeadLine());
        homework.setModifiedTime(System.currentTimeMillis());
        homework.setSaved(homeworkDTO.getIsSaved());
        homeworkDao.save(homework);
        modifyUrlOfHomework(homeworkDTO);
    }

    //根据提交的homeworkDTO修改对应的附件信息
    private void modifyUrlOfHomework(HomeworkDTO homeworkDTO){
        //检查附件是否有变化，如果有新添加的附件，将作业的ID添加到新加的附件记录中，如果有附件被从作业的附件列表中删除，删除记录和文件
        List<Long> attachmentIds=homeworkDTO.getAttachmentIds();      //现在的附件IDlist
        //数据库中的关联到这个作业的附件IDlist
        List<Long> originalIds=attachmentService.getAttachmentIDsByTypeAndRelatedId(CommonConst.ATTACH_TYPE_HOMEWORK,homeworkDTO.getId());
        //新添加的附件IDlist
        List<Long> toAddIds=attachmentIds.stream().filter(id->!originalIds.contains(id)).collect(Collectors.toList());
        //新添加的网盘文件
        if(!CollectionUtils.isEmpty(homeworkDTO.getAttachmentUrls()))
            toAddIds.addAll(attachmentService.makeAttachmentsByUrls(homeworkDTO.getAttachmentUrls()));
        //待删除的IDlist
        List<Long> toRemoveIds=originalIds.stream().filter(id->!attachmentIds.contains(id)).collect(Collectors.toList());
        if(!CollectionUtils.isEmpty(toAddIds))
            toAddIds.stream().forEach(id->attachmentService.setTypeAndRelatedId(id,CommonConst.ATTACH_TYPE_HOMEWORK,homeworkDTO.getId()));
        if(!CollectionUtils.isEmpty(toRemoveIds))
            toRemoveIds.stream().forEach(id->attachmentService.deleteAttachmentById(id));
    }

    @Override
    public void deleteHomework(Long id){
        Homework homework=homeworkDao.findOne(id);
        if(Objects.nonNull(homework)){
            this.deleteHomework(homework);
            homeworkDao.save(homework);
        }else{
            throw new AppException(ErrorCodes.HOMEWORK_NOT_FOUND);
        }
    }

    @Override
    public void deleteHomeworkByGroupId(List<Long> groupIds){
        groupIds.forEach(groupId->{
            List<Homework> homeworks = new ArrayList<>();
            if(!CollectionUtils.isEmpty(homeworks)){
                homeworks.forEach(homework -> this.deleteHomework(homework));
                homeworkDao.save(homeworks);
            }
        });
    }

    private void deleteHomework(Homework homework){
        homework.setDeleted(CommonConst.DELETED);
        Long id = homework.getId();
        //删除该作业下的附件
        attachmentService.deleteAttachmentsByTypeAndRelatedId(CommonConst.ATTACH_TYPE_HOMEWORK,id);
        //删除该作业下学生提交的作业，包括数据库记录和附件需要异步调用
        homeworkSubmissionService.deleteHomeworkSubmissions(id);
    }

    @Override
    public List<HomeworkDTO> queryHomeworkDTOsByGroupId(Long groupId){
        List<Homework> homeworks=homeworkDao.queryByGroupId(groupId);
        List<HomeworkDTO> homeworkDTOS=new ArrayList<>();
        if(!Collections.isEmpty(homeworks))
        homeworkDTOS = homeworks.stream().map(homework->this.convertFromHomeworkToHomeworkDTO(homework,false))
                .collect(Collectors.toList());
        return homeworkDTOS;
    }

    @Override
    public List<Homework> queryHomeworksByGroupId(Long groupId) {
        return homeworkDao.queryByGroupIdAndDeleted(groupId);
    }
}
