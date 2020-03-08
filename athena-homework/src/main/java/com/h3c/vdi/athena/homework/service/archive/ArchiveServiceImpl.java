package com.h3c.vdi.athena.homework.service.archive;

import com.h3c.vdi.athena.common.concurrent.VDIExecutorServices;
import com.h3c.vdi.athena.common.config.SSHConfig;
import com.h3c.vdi.athena.common.exception.AppException;
import com.h3c.vdi.athena.common.utils.JsonUtils;
import com.h3c.vdi.athena.common.utils.RedisUtil;
import com.h3c.vdi.athena.common.utils.SSHExecutor;
import com.h3c.vdi.athena.homework.constant.CommonConst;
import com.h3c.vdi.athena.homework.dao.*;
import com.h3c.vdi.athena.homework.entity.*;
import com.h3c.vdi.athena.homework.feign.keystone.UserFeignService;
import com.jcraft.jsch.JSchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

/**
 * Created by w16051 on 2018/10/13.
 */
@Service
public class ArchiveServiceImpl implements ArchiveService {

    @Resource
    private ClassEntityDao classEntityDao;

    @Resource
    private UserClassRelationDao userClassRelationDao;

    @Resource
    private UserFeignService userFeignService;

    @Resource
    private HomeworkSubmissionDao homeworkSubmissionDao;

    @Resource
    private HomeworkPraiseDao homeworkPraiseDao;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private AttachmentDao attachmentDao;

    private Logger logger= LoggerFactory.getLogger(ArchiveServiceImpl.class);

    /**
     * 线程池
     */
    private ExecutorService ioService = VDIExecutorServices.get().getIoBusyService();

    @Override
    public void archiveDataByYear(String year){

        ioService.submit(()->{
            //根据入学年份找出符合条件的班级，然后根据班级获得学生，然后获得学生提交的作业与附件
            String startTime = year+"-01-01";
            String endTime = year+"-12-31";
            List<ClassEntity> classEntities = classEntityDao.findClassesByStartTimeAndEndTime(startTime,endTime);
            classEntities.stream().forEach(classEntity -> archiveDataByClass(classEntity));
        });

    }

    private void archiveDataByClass(ClassEntity classEntity){
        classEntity.setDeleted(CommonConst.ARCHIVED);
        classEntityDao.save(classEntity);
        List<Long> userIds = this.archiveUserClassRelation(classEntity.getId());
        try {
            this.archiveByUserIds(userIds);
        }catch (Exception e){
            logger.warn("ssh连接服务器失败，无法删除附件");
            e.printStackTrace();
            throw new AppException("删除附件失败，具体错误请查看日志文件");
        }
    }

    //将user和class中间表数据归档并返回该班级下的所有学生的id
    private List<Long> archiveUserClassRelation(Long classId){
        List<UserClassRelation> userClassRelations = userClassRelationDao.findByClassId(classId);
        List<Long> userIds= userClassRelations.stream().map(userClassRelation -> this.handleArchiveUserClassRelation(userClassRelation)).collect(Collectors.toList());
        userClassRelationDao.save(userClassRelations);
        return userIds;
    }

    private Long handleArchiveUserClassRelation(UserClassRelation userClassRelation){
        userClassRelation.setDeleted(CommonConst.ARCHIVED);
        return userClassRelation.getUserId();
    }

    //归档学生、学生提交的作业、点赞数据，删除附件，删除nextcloud账号及下属文件
    private void archiveByUserIds(List<Long> userIds) throws JSchException {
        //归档学生数据,删除nextcloud账号及下属文件
        userFeignService.archiveUsers(userIds);
        //归档提交作业
        List<HomeworkSubmission> homeworkSubmissions = homeworkSubmissionDao.queryByUserIds(userIds);
        homeworkSubmissions.forEach(homeworkSubmission -> homeworkSubmission.setDeleted(CommonConst.ARCHIVED));
        homeworkSubmissionDao.save(homeworkSubmissions);
        //归档点赞数据
        List<HomeworkPraise> homeworkPraises = homeworkPraiseDao.findByUserIds(userIds);
        homeworkPraises.forEach(homeworkPraise -> homeworkPraise.setDeleted(CommonConst.ARCHIVED));
        homeworkPraiseDao.save(homeworkPraises);
        //删除附件
        List<Long> homeworkSubmissionIds = homeworkSubmissions.stream().map(homeworkSubmission -> homeworkSubmission.getId()).collect(Collectors.toList());
        this.removeAttachments(homeworkSubmissionIds);
    }

    //删除附件
    private void removeAttachments(List<Long> homeworkSubmissionIds) throws JSchException {
        List<AttachmentEntity> attachmentEntities = attachmentDao
                .queryAttachmentsByTypeAndRelatedIdInAndDeleted(CommonConst.ATTACH_TYPE_HOMEWORK_SUBMISSION,homeworkSubmissionIds,CommonConst.NOT_DELETED);
        //归档附件数据库记录
        attachmentEntities.forEach(attachmentEntity -> attachmentEntity.setDeleted(CommonConst.ARCHIVED));
        //获取附件的url，用ssh连接和shell指令删除附件
        List<String> urls = attachmentEntities.stream().map(attachmentEntity -> attachmentEntity.getUrl()).collect(Collectors.toList());
        SSHConfig sshConfig = JsonUtils.parseSSHConfig(redisUtil.get(RedisUtil.REDIS_KEY_SSHCONFIG));
        SSHExecutor sshExecutor = SSHExecutor.newInstance(sshConfig);
        urls.forEach(url->{
            try {
                this.removeAttachment(url,sshExecutor);
            }catch (Exception e) {
                logger.warn("删除附件命令执行失败");
                e.printStackTrace();
                throw new AppException("删除附件失败，具体错误请查看日志文件");
            }
        });
        sshExecutor.close();
    }

    //删除附件具体操作
    private void removeAttachment(String url,SSHExecutor sshExecutor) throws InterruptedException, JSchException, IOException {
        String cmd = "rm -f " + url;
        sshExecutor.exec(cmd);
    }


    @Override
    public List<String> queryYears(){
        List<ClassEntity> classEntities = classEntityDao.findAll();
        List<String> years = classEntities
                .stream().map(classEntity -> this.getYear(classEntity.getCreateTime())).distinct().collect(Collectors.toList());
        return years;
    }

    private String getYear(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return String.valueOf(calendar.get(Calendar.YEAR));
    }
}
