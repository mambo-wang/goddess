package com.h3c.vdi.athena.homework.service.homework;

import com.h3c.vdi.athena.homework.dto.HomeworkDTO;
import com.h3c.vdi.athena.homework.entity.Homework;

import java.util.List;

/**
 * Created by w16051 on 2018/3/21.
 */
public interface HomeworkService {

    /**
     * 查询当前登录人发布或需要完成的作业
     * @return
     */
    List<HomeworkDTO> queryHomeworkDTOs();

    /**
     * 查询作业详情
     * @param Id
     * @return
     */
    HomeworkDTO queryHomeworkDTOById(Long Id);

    /**
     * 发布作业
     * @param homeworkDTO
     */
    void addHomework(HomeworkDTO homeworkDTO);

    /**
     * 根据课程组和时间查询作业
     *
     * @param groupId   课程组id
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return
     */
    List<Homework> queryHomeworkByLessonGroupAndDate(Long groupId, Long startDate, Long endDate);

    /**
     * 根据作业名称查询作业
     *
     * @param name 作业名称
     * @return 作业
     */
    Homework queryHomeworkByName(String name);

    /**
     * 修改作业作业
     * @param homeworkDTO
     */
    void modifyHomework(HomeworkDTO homeworkDTO);

    /**
     * 删除作业
     * todo  需要异步调用
     * @param id
     */
    void deleteHomework(Long id);

    /**
     * 根据groupid删除作业
     */
    void deleteHomeworkByGroupId(List<Long> groupIds);

    /**
     * 根据课程组ID来查询作业DTO
     * @param groupId
     * @return
     */
    List<HomeworkDTO> queryHomeworkDTOsByGroupId(Long groupId);

    /*****************************************************数据统计使用********************************************
     * 根据课程组ID来查询作业
     * @param groupId
     * @return
     */
    List<Homework> queryHomeworksByGroupId(Long groupId);


}
