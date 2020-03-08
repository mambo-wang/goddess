package com.h3c.vdi.athena.homework.service.homeworkSubmission;

import com.h3c.vdi.athena.common.exception.AppException;
import com.h3c.vdi.athena.homework.constant.CommonConst;
import com.h3c.vdi.athena.homework.dao.AttachmentDao;
import com.h3c.vdi.athena.homework.dao.HomeworkDao;
import com.h3c.vdi.athena.homework.dao.HomeworkSubmissionDao;
import com.h3c.vdi.athena.homework.dto.*;
import com.h3c.vdi.athena.homework.entity.AttachmentEntity;
import com.h3c.vdi.athena.homework.entity.Homework;
import com.h3c.vdi.athena.homework.entity.HomeworkSubmission;
import com.h3c.vdi.athena.homework.exception.ErrorCodes;
import com.h3c.vdi.athena.homework.service.attachment.AttachmentService;
import com.h3c.vdi.athena.homework.service.homework.HomeworkService;
import com.h3c.vdi.athena.homework.service.user.UserService;
import io.jsonwebtoken.lang.Collections;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by w16051 on 2018/3/26.
 */
@Service
public class HomeworkSubmissionServiceImpl implements HomeworkSubmissionService {

    @Resource
    private HomeworkSubmissionDao homeworkSubmissionDao;

    @Resource
    private AttachmentDao attachmentDao;

    @Resource
    private AttachmentService attachmentService;

    @Resource
    private HomeworkSubmissionDao submissionDao;

    @Resource
    private HomeworkDao homeworkDao;

    @Resource
    private UserService userService;

    @Resource
    private HomeworkService homeworkService;

    private Logger logger= LoggerFactory.getLogger(HomeworkSubmissionServiceImpl.class);

    @Override
    public List<HomeworkSubmission> querySubmitByHomeworkId(Long homeworkId) {
        return submissionDao.queryHomeworkSubmissionByHomeworkId(homeworkId);
    }

    @Override
    public HomeworkSubmission querySubmitByHomeworkIdAndUserId(Long homeworkId, Long userId) {
        return submissionDao.queryByHomeworkIdAndUserId(homeworkId, userId);
    }



    @Override
    public void deleteHomeworkSubmissions(Long homeworkId){
        List<HomeworkSubmission> homeworkSubmissions= homeworkSubmissionDao.queryHomeworkSubmissionByHomeworkId(homeworkId);
        if(!CollectionUtils.isEmpty(homeworkSubmissions))
            this.deleteHomeworkSubmissions(homeworkSubmissions);
    }

    private void deleteHomeworkSubmissions(List<HomeworkSubmission> homeworkSubmissions){
        if(!CollectionUtils.isEmpty(homeworkSubmissions)) {
            //1、删除提交作业记录，将deleted置为'y'
            homeworkSubmissions.stream().forEach(homeworkSubmission -> homeworkSubmission.setDeleted(CommonConst.DELETED));
            homeworkSubmissionDao.save(homeworkSubmissions);
            //删除提交作业下的附件
            List<Long> homeworkSubmissionIds = homeworkSubmissions.stream()
                    .map(homeworkSubmission -> homeworkSubmission.getId())
                    .collect(Collectors.toList());
            deleteHomeworkAttachments(homeworkSubmissionIds);
        }
    }

    /**
     * 删除作业下的附件
     */
    private void deleteHomeworkAttachments(List<Long> ids){
        List<AttachmentEntity> attachments=ids.stream()
                .map(id->attachmentDao.queryAttachmentsByTypeAndRelatedIdAndDeleted(CommonConst.ATTACH_TYPE_HOMEWORK_SUBMISSION,id,CommonConst.NOT_DELETED))
                .flatMap(Collection::stream)
                .filter(n-> Objects.nonNull(n))
                .collect(Collectors.toList());
        attachments.stream().forEach(attachmentEntity -> attachmentService.deleteAttachment(attachmentEntity));
    }

    @Override
    public void deleteHomeworkSubmissionsByUserId(Long userId){
        List<HomeworkSubmission> homeworkSubmissions = homeworkSubmissionDao.queryByUserId(userId);
        if(!CollectionUtils.isEmpty(homeworkSubmissions))
            this.deleteHomeworkSubmissions(homeworkSubmissions);
    }

    @Override
    public List<HomeworkSubDTO> queryHomeworkSubDTOByHomeworkId(Long homeworkId){
        List<HomeworkSubmission> homeworkSubmissions=homeworkSubmissionDao.queryHomeworkSubmissionByHomeworkId(homeworkId);
        List<HomeworkSubDTO> homeworkSubDTOS=new ArrayList<>();
        if(!CollectionUtils.isEmpty(homeworkSubmissions))
        {
            homeworkSubDTOS=homeworkSubmissions.stream().map(h->this.convertFromHomeworkSubmissionToDTO(h,false)).collect(Collectors.toList());
        }
        return homeworkSubDTOS;
    }

    private HomeworkSubDTO convertFromHomeworkSubmissionToDTO(HomeworkSubmission homeworkSubmission,boolean ifSingle){
        HomeworkSubDTO homeworkSubDTO=new HomeworkSubDTO();
        UserDTO userDTO=userService.queryUserById(homeworkSubmission.getUserId());
        homeworkSubDTO.setId(homeworkSubmission.getId());
        homeworkSubDTO.setUser(userDTO);
        homeworkSubDTO.setSubmitTime(homeworkSubmission.getSubmitTime());
        homeworkSubDTO.setScoreTime(homeworkSubmission.getScoreTime());
        homeworkSubDTO.setScore(homeworkSubmission.getScore());
        homeworkSubDTO.setStarred(homeworkSubmission.getStarred());
        homeworkSubDTO.setHomeworkName(homeworkSubmission.getHomework().getName());
        if(ifSingle)
        {
            homeworkSubDTO.setAnswer(homeworkSubmission.getAnswer());
            homeworkSubDTO.setComment(homeworkSubmission.getComment());
            //设置附件
            List<AttachmentDTO> attachmentDTOS=attachmentService.getAttachmentDTOsByTypeAndRelatedId(CommonConst.ATTACH_TYPE_HOMEWORK_SUBMISSION,homeworkSubmission.getId());
            homeworkSubDTO.setAttachmentDTOS(attachmentDTOS);
            //设置下发作业信息
            HomeworkDTO homeworkDTO= homeworkService.queryHomeworkDTOById(homeworkSubmission.getHomework().getId());
            homeworkSubDTO.setHomework(homeworkDTO);
        }
        return homeworkSubDTO;
    }

    @Override
    public void changeShowStatus(ShowHomeworkInfoDTO showHomeworkInfoDTO){
        HomeworkSubmission homeworkSubmission=homeworkSubmissionDao.getOne(showHomeworkInfoDTO.getHomeworkSubId());
        if(Objects.isNull(homeworkSubmission)){
            logger.warn("find no matched homeworkSubmission, please try to refresh and try again");
            throw new AppException(ErrorCodes.FIND_NO_HOMEWORK_SUBMISSION);
        }
        homeworkSubmission.setStarred(showHomeworkInfoDTO.getStatus());
        homeworkSubmissionDao.save(homeworkSubmission);
    }

    @Override
    public HomeworkSubDTO queryHomeworkSubDTOById(Long id){
        HomeworkSubmission homeworkSubmission = homeworkSubmissionDao.getOne(id);
        if(Objects.isNull(homeworkSubmission)){
            logger.warn("find no matched homeworkSubmission, please try to refresh and try again");
            throw new AppException(ErrorCodes.FIND_NO_HOMEWORK_SUBMISSION);
        }
        HomeworkSubDTO homeworkSubDTO = convertFromHomeworkSubmissionToDTO(homeworkSubmission,true);
        return homeworkSubDTO;
    }

    @Override
    public HomeworkSubmission queryHomeworkSubById(Long id) {
        HomeworkSubmission homeworkSubmission = homeworkSubmissionDao.getOne(id);
        if(Objects.isNull(homeworkSubmission)){
            logger.warn("find no matched homeworkSubmission, please try to refresh and try again");
            throw new AppException(ErrorCodes.FIND_NO_HOMEWORK_SUBMISSION);
        }
        return homeworkSubmission;
    }

    @Override
    public void setScore(HomeworkSubDTO homeworkSubDTO){
        HomeworkSubmission homeworkSubmission=homeworkSubmissionDao.getOne(homeworkSubDTO.getId());
        if(Objects.isNull(homeworkSubmission)){
            logger.warn("find no matched homeworkSubmission, please try to refresh and try again");
            throw new AppException(ErrorCodes.FIND_NO_HOMEWORK_SUBMISSION);
        }
        if(Objects.isNull(homeworkSubDTO.getScore())){
            logger.warn("score is null,please check it");
            throw new AppException(ErrorCodes.HOMEWORK_NO_SCORE);
        }
        homeworkSubmission.setScore(homeworkSubDTO.getScore());
        homeworkSubmission.setComment(homeworkSubDTO.getComment());
        homeworkSubmissionDao.save(homeworkSubmission);
    }

    @Override
    public List<HomeworkSubDTO> queryShowedHomeworkSubDTOs(Long homeworkId){
        List<HomeworkSubmission> homeworkSubmissions=homeworkSubmissionDao.queryStarredHomeworkSubmissionByHomeworkId(homeworkId);
        if(CollectionUtils.isEmpty(homeworkSubmissions))
            return new ArrayList<>();
        List<HomeworkSubDTO> homeworkSubDTOS=homeworkSubmissions.stream()
                .map(homeworkSubmission -> convertFromHomeworkSubmissionToDTO(homeworkSubmission,false))
                .collect(Collectors.toList());
        return homeworkSubDTOS;
    }

    @Override
    public HomeworkSubDTO queryByHomeworkIdAndCurrentUser(Long homeworkId){
        Long currentUserId=userService.currentLoginUser().getId();
        HomeworkSubmission homeworkSubmission=homeworkSubmissionDao.queryByHomeworkIdAndUserId(homeworkId,currentUserId);
        HomeworkSubDTO homeworkSubDTO=new HomeworkSubDTO();
        //如果还没有交作业，则只返回作业题目详情，其他为空
        if(Objects.isNull(homeworkSubmission)){
            HomeworkDTO homeworkDTO=homeworkService.queryHomeworkDTOById(homeworkId);
            homeworkSubDTO.setHomework(homeworkDTO);
            return homeworkSubDTO;
        }
        homeworkSubDTO = convertFromHomeworkSubmissionToDTO(homeworkSubmission,true);
        return homeworkSubDTO;
    }

    @Override
    public void submitHomework(HomeworkSubDTO homeworkSubDTO){
        judgeHomeworkSubDTO(homeworkSubDTO);
        Long curUserId=userService.currentLoginUser().getId();
        HomeworkSubmission homeworkSubmission=homeworkSubmissionDao.queryByHomeworkIdAndUserId(homeworkSubDTO.getHomework().getId(),curUserId);
        if(Objects.nonNull(homeworkSubmission)) {
            updateHomeworkSubmission(homeworkSubDTO);
        }
        else {
            HomeworkSubmission homeworkSub=new HomeworkSubmission();
            homeworkSub.setUserId(curUserId);
            homeworkSub.setSubmitTime(System.currentTimeMillis());
            homeworkSub.setAnswer(homeworkSubDTO.getAnswer());
            homeworkSub.setStarred(CommonConst.NOT_SHOW_HOMEWORK);
            homeworkSub.setDeleted(CommonConst.NOT_DELETED);
            Homework homework=homeworkDao.findOne(homeworkSubDTO.getHomework().getId());
            homeworkSub.setHomework(homework);
            HomeworkSubmission homeworkSubmissionSaved=homeworkSubmissionDao.save(homeworkSub);
            //处理附件，将已上传的附件关联到新建的作业记录
            List<Long> attachmentIds = homeworkSubDTO.getAttachmentIds();
            //新添加的网盘文件
            if(!CollectionUtils.isEmpty(homeworkSubDTO.getAttachmentUrls()))
                attachmentIds.addAll(attachmentService.makeAttachmentsByUrls(homeworkSubDTO.getAttachmentUrls()));
            if(!CollectionUtils.isEmpty(attachmentIds))
                attachmentIds.stream().forEach(id->attachmentService.setTypeAndRelatedId(id,CommonConst.ATTACH_TYPE_HOMEWORK_SUBMISSION,homeworkSubmissionSaved.getId()));
        }
    }

    //判断提交作业信息是否合法
    private void judgeHomeworkSubDTO(HomeworkSubDTO homeworkSubDTO){
        if ((Collections.isEmpty(homeworkSubDTO.getAttachmentIds())&&Objects.isNull(homeworkSubDTO.getAnswer()))){
            logger.warn("can't add homeworkSubmission because answer and attachments are empty");
            throw new AppException(ErrorCodes.HOMEWORK_SUBMISSION_NOT_LEGAL);
        }
    }

    @Override
    public void updateHomeworkSubmission(HomeworkSubDTO homeworkSubDTO){
        judgeHomeworkSubDTO(homeworkSubDTO);
        HomeworkSubmission homeworkSubmission=homeworkSubmissionDao.getOne(homeworkSubDTO.getId());
        Homework homework=homeworkSubmission.getHomework();
        Long curTime=System.currentTimeMillis();
        if(curTime>homework.getDeadLine()){
            logger.warn("it's after deadline, can't submit homework answer now");
            throw new AppException(ErrorCodes.TIME_OVER_DEADLINE);
        }
        homeworkSubmission.setAnswer(homeworkSubDTO.getAnswer());
        homeworkSubmission.setSubmitTime(System.currentTimeMillis());
        modifyUrlOfHomeworkSubmission(homeworkSubDTO);
    }

    //根据提交的homeworkDTO修改对应的附件信息
    private void modifyUrlOfHomeworkSubmission(HomeworkSubDTO homeworkSubDTO){
        //检查附件是否有变化，如果有新添加的附件，将作业的ID添加到新加的附件记录中，如果有附件被从作业的附件列表中删除，删除记录和文件
        List<Long> attachmentIds=homeworkSubDTO.getAttachmentIds();      //现在的附件IDlist
        //数据库中的关联到这个作业的附件IDlist
        List<Long> originalIds=attachmentService.getAttachmentIDsByTypeAndRelatedId(CommonConst.ATTACH_TYPE_HOMEWORK_SUBMISSION,homeworkSubDTO.getId());
        //新添加的附件IDlist
        List<Long> toAddIds=attachmentIds.stream().filter(id->!originalIds.contains(id)).collect(Collectors.toList());
        //新添加的网盘文件
        if(!CollectionUtils.isEmpty(homeworkSubDTO.getAttachmentUrls()))
            toAddIds.addAll(attachmentService.makeAttachmentsByUrls(homeworkSubDTO.getAttachmentUrls()));
        //待删除的IDlist
        List<Long> toRemoveIds=originalIds.stream().filter(id->!attachmentIds.contains(id)).collect(Collectors.toList());
        if(!CollectionUtils.isEmpty(toAddIds))
            toAddIds.stream().forEach(id->attachmentService.setTypeAndRelatedId(id,CommonConst.ATTACH_TYPE_HOMEWORK_SUBMISSION,homeworkSubDTO.getId()));
        if(!CollectionUtils.isEmpty(toRemoveIds))
            toRemoveIds.stream().forEach(id->attachmentService.deleteAttachmentById(id));
    }

    @Override
    public List<HomeworkSubDTO> queryShowedHomeworkSubDTOsByGroupId(Long groupId){
        List<Long> homeworkIds = homeworkDao.queryByGroupId(groupId)
                .stream().map(homework -> homework.getId()).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(homeworkIds)){
            return new ArrayList<>();
        }else{
            return homeworkIds.stream().map(id->this.queryShowedHomeworkSubDTOs(id))
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<HomeworkSubmission> queryByGroupIdAndStuIdAndTime(Long lessonGroupId, Long startDate, Long endDate, Long useId) {
        return homeworkSubmissionDao.queryByGroupIdAndStuIdAndTime(lessonGroupId, startDate, endDate, useId);
    }

    @Override
    public List<HomeworkSubmission> queryByStuIdAndTime(Long startDate, Long endDate, Long useId) {
        return homeworkSubmissionDao.queryByStuIdAndTime(startDate, endDate, useId);
    }

    @Override
    public List<HomeworkSubmission> queryByGroupIdAndTime(Long groupId, Long startDate, Long endDate) {
        return homeworkSubmissionDao.queryByGroupIdAndTime(groupId, startDate, endDate);
    }

    @Override
    public List<HomeworkSubmission> queryAllByGroupIdAndTime(Long groupId, Long startDate, Long endDate) {
        return homeworkSubmissionDao.queryAllByGroupIdAndTime(groupId, startDate, endDate);
    }

    @Override
    public List<HomeworkSubmission> queryByGroupIdAndHomeworkIdAndTime(Long lessonGroupId, Long startDate, Long endDate, Long homeworkId) {
        return homeworkSubmissionDao.queryByGroupIdAndHomeworkIdAndTime(lessonGroupId, startDate, endDate, homeworkId);
    }
}
