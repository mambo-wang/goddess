package com.h3c.vdi.athena.homework.service.homeworkSubmission;


import com.h3c.vdi.athena.homework.dto.HomeworkSubDTO;
import com.h3c.vdi.athena.homework.dto.ShowHomeworkInfoDTO;
import com.h3c.vdi.athena.homework.entity.HomeworkSubmission;

import java.util.List;

/**
 * Created by w16051 on 2018/3/26.
 */
public interface HomeworkSubmissionService {

    /**
     * 删除提交作业
     * 需要时间较长，调用处要异步调用
     * @param homeworkId 所属老师下发作业的ID
     */
    void deleteHomeworkSubmissions(Long homeworkId);

    /**
     * 删除某个学生所有提交的作业
     * @param userId
     */
    void deleteHomeworkSubmissionsByUserId(Long userId);

    /**
     * 根据HomeworkId查询已提交的作业
     *
     * @param homeworkId 作业id
     * @return 已提交作业
     */
    List<HomeworkSubmission> querySubmitByHomeworkId(Long homeworkId);

    /**
     * 根据HomeworkId和用户id查询已提交的作业
     *
     * @param homeworkId 作业id
     * @param userId     用户id
     * @return 已提交作业
     */
    HomeworkSubmission querySubmitByHomeworkIdAndUserId(Long homeworkId, Long userId);

    /**
     * 根据homeworkId查询已提交的作业列表提供前台展示
     * @param homeworkId
     * @return
     */
    List<HomeworkSubDTO> queryHomeworkSubDTOByHomeworkId(Long homeworkId);

    /**
     * 修改是否展示作业
     * @param showHomeworkInfoDTO
     */
    void changeShowStatus(ShowHomeworkInfoDTO showHomeworkInfoDTO);

    /**
     * 根据Id查询已提交的作业详情提供前台展示
     * @param id
     * @return
     */
    HomeworkSubDTO queryHomeworkSubDTOById(Long id);

    /**
     * 根据Id查询已提交的作业详情
     * @param id
     * @return
     */
    HomeworkSubmission queryHomeworkSubById(Long id);

    /**
     * 设置作业分数
     * @param homeworkSubDTO
     */
    void setScore(HomeworkSubDTO homeworkSubDTO);

    /**
     * 根据作业ID查找被展示的作业
     * @param homeworkId
     * @return
     */
    List<HomeworkSubDTO> queryShowedHomeworkSubDTOs(Long homeworkId);


    /**
     * 根据老师布置作业的ID查询当前登录学生作业提交详情
     * @param homeworkId
     * @return
     */
    HomeworkSubDTO queryByHomeworkIdAndCurrentUser(Long homeworkId);

    /**
     * 学生提交作业
     * @param homeworkSubDTO
     */
    void submitHomework(HomeworkSubDTO homeworkSubDTO);

    /**
     * 学生修改作业
     * deadline之后无法修改
     * @param homeworkSubDTO
     */
    void updateHomeworkSubmission(HomeworkSubDTO homeworkSubDTO);

    /**
     * 根据课程组ID来查询所有被展示作业DTO
     * @param groupId
     * @return
     */
    List<HomeworkSubDTO> queryShowedHomeworkSubDTOsByGroupId(Long groupId);

    /**********************************************************数据统计使用**************************************************
     * 根据课程组id、起止时间、学生id查找已提交、已打分作业
     * @param lessonGroupId 课程组id
     * @param startDate 起始  作业下发时间
     * @param endDate 结束  作业下发时间
     * @param useId 学生id
     * @return 已提交作业
     */
    List<HomeworkSubmission> queryByGroupIdAndStuIdAndTime(Long lessonGroupId, Long startDate, Long endDate, Long useId);

    /**
     * 起止时间、学生id查找已提交、已打分作业
     * @param startDate 起始  作业下发时间
     * @param endDate 结束  作业下发时间
     * @param useId 学生id
     * @return 已提交作业
     */
    List<HomeworkSubmission> queryByStuIdAndTime( Long startDate, Long endDate, Long useId);

    /**
     * 起止时间、学生id查找已提交、已打分作业
     * @param startDate 起始  作业下发时间
     * @param endDate 结束  作业下发时间
     * @param groupId 分组id
     * @return 已提交作业
     */
    List<HomeworkSubmission> queryByGroupIdAndTime(Long groupId, Long startDate, Long endDate);

    /**
     * 起止时间、学生id查找已提交、已打分作业
     * @param startDate 起始  作业下发时间
     * @param endDate 结束  作业下发时间
     * @param groupId 分组id
     * @return 已提交作业
     */
    List<HomeworkSubmission> queryAllByGroupIdAndTime(Long groupId, Long startDate, Long endDate);

    /**
     * 起止时间、学生id查找已提交、已打分作业
     * @param lessonGroupId 课程组id
     * @param startDate 起始  作业下发时间
     * @param endDate 结束  作业下发时间
     * @param homeworkId 作业id
     * @return 已提交作业
     */
    List<HomeworkSubmission> queryByGroupIdAndHomeworkIdAndTime(Long lessonGroupId, Long startDate, Long endDate, Long homeworkId);
}
