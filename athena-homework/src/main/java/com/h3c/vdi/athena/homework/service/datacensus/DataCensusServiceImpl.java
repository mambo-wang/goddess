package com.h3c.vdi.athena.homework.service.datacensus;

import com.h3c.vdi.athena.common.utils.StringManager;
import com.h3c.vdi.athena.homework.dto.DataCensusScoresDTO;
import com.h3c.vdi.athena.homework.dto.DataCensusDTO;
import com.h3c.vdi.athena.homework.dto.UserDTO;
import com.h3c.vdi.athena.homework.entity.Homework;
import com.h3c.vdi.athena.homework.entity.HomeworkSubmission;
import com.h3c.vdi.athena.homework.service.homework.HomeworkService;
import com.h3c.vdi.athena.homework.service.homeworkSubmission.HomeworkSubmissionService;
import com.h3c.vdi.athena.homework.service.lessongroup.LessonGroupService;
import com.h3c.vdi.athena.homework.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service("dataCensusService")
public class DataCensusServiceImpl implements DataCensusService {

    @Resource
    private LessonGroupService lessonGroupService;

    @Resource
    private UserService userService;

    @Resource
    private HomeworkService homeworkService;

    @Resource
    private HomeworkSubmissionService homeworkSubmissionService;

    private StringManager sm = StringManager.getManager("Statistics");

    @Override
    public List<DataCensusDTO> queryHomeworkCompleteResult(Long lessonGroupId, Long startDate, Long endDate) {
        List<HomeworkSubmission> homeworkSubmits = queryAllHomeworkSubmissionsByGroupId(lessonGroupId, startDate, endDate);
        if (CollectionUtils.isEmpty(homeworkSubmits)) {
            return Collections.emptyList();
        }

        //将所有已提交作业按照提交作业的用户的ID分组,再按照
        Map<Long, Map<Long, List<HomeworkSubmission>>> groupByUserId = homeworkSubmits.stream()
                .collect(Collectors.groupingBy(HomeworkSubmission::getUserId,
                        Collectors.groupingBy(HomeworkSubmission::getHomeworkId)));

        //课程组中的所有用户
        List<UserDTO> userDTOS = lessonGroupService.queryUserNotDeletedByGroup(lessonGroupId);
        List<Homework> homeworks = homeworkService.queryHomeworksByGroupId(lessonGroupId);

        List<DataCensusDTO> list = new ArrayList<>();
        for (UserDTO user : userDTOS){
            DataCensusDTO dataCensusDTO = new DataCensusDTO();
            dataCensusDTO.setUserInfo(user.getName());

            //键为作业id，值为
            Map<Long, List<HomeworkSubmission>> groupByHomeworkId = groupByUserId.get(user.getId());

            //该学生未提交过作业
            if(CollectionUtils.isEmpty(groupByHomeworkId)){
                List<DataCensusScoresDTO> dataCensusScoresDTOS = homeworks.stream()
                        .map(homework -> new DataCensusScoresDTO(homework.getName(), sm.getString("teacher.table.uncommitted")))
                        .collect(Collectors.toList());
                dataCensusDTO.setScores(dataCensusScoresDTOS);
                dataCensusDTO.setAverageScore(0.0);
                dataCensusDTO.setExcellentCount(0);
            } else {
                List<DataCensusScoresDTO> dataCensusScoresDTOS = new ArrayList<>(homeworks.size());
                double totalScore = 0.0;
                int excellentCount = 0;
                //有评分的作业数量
                int scoreCount = 0;
                for (Homework homework : homeworks){

                    DataCensusScoresDTO dataCensusScoresDTO = new DataCensusScoresDTO();
                    List<HomeworkSubmission> homeworkSubmissions = groupByHomeworkId.get(homework.getId());

                    //学生没提交本次作业
                    if(CollectionUtils.isEmpty(homeworkSubmissions)){
                        dataCensusScoresDTO.setName(homework.getName());
                        dataCensusScoresDTO.setScore(sm.getString("teacher.table.uncommitted"));
                    }else {
                        HomeworkSubmission homeworkSubmission = homeworkSubmissions.get(0);
                        dataCensusScoresDTO.setName(homework.getName());
                        if(Objects.isNull(homeworkSubmission.getScore())){
                            dataCensusScoresDTO.setScore(sm.getString("teacher.table.unscored"));
                        } else {
                            totalScore += homeworkSubmission.getScore();
                            scoreCount += 1;
                            if(homeworkSubmission.getScore() >= 9){
                                excellentCount += 1;
                            }
                            dataCensusScoresDTO.setScore(String.valueOf(homeworkSubmission.getScore()));
                        }
                    }
                    dataCensusScoresDTOS.add(dataCensusScoresDTO);
                }
                dataCensusDTO.setScores(dataCensusScoresDTOS);
                dataCensusDTO.setExcellentCount(excellentCount);
                dataCensusDTO.setAverageScore(totalScore / scoreCount);
            }
            list.add(dataCensusDTO);
        }

        //排序，按平均分和优秀作业数排序
        return list.stream()
                .sorted(Comparator.comparingDouble(DataCensusDTO::getAverageScore)
                .thenComparing(Comparator.comparingInt(DataCensusDTO::getExcellentCount))
                .reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<DataCensusScoresDTO> queryAverageScore(Long lessonGroupId, Long startDate, Long endDate) {
        List<HomeworkSubmission> homeworkSubmits = queryHomeworkSubmissionsByHomeworkId(lessonGroupId, startDate, endDate, null);
        if (CollectionUtils.isEmpty(homeworkSubmits)) {
            return Collections.emptyList();
        }
        //将所有已提交作业按照作业的ID分组
        Map<Long, List<HomeworkSubmission>> map = homeworkSubmits.stream().collect(Collectors.groupingBy(x -> x.getHomework().getId()));

        List<DataCensusScoresDTO> scoresList = new ArrayList<>();
        for (Long homeworkId : map.keySet()) {
            List<HomeworkSubmission> homeworkSubmissions = map.get(homeworkId);

            Double totalScore = 0.0;
            for (HomeworkSubmission homeworkSubmit : homeworkSubmissions) {
                totalScore += homeworkSubmit.getScore();
            }
            DataCensusScoresDTO scoresDTO = new DataCensusScoresDTO();
            scoresDTO.setName(homeworkService.queryHomeworkDTOById(homeworkId).getName());
            scoresDTO.setScore(String.valueOf(totalScore / homeworkSubmissions.size()));
            scoresList.add(scoresDTO);
        }
        return scoresList;
    }

    /**
     * 已改
     * 根据课程分组、开始时间、结束时间查询所有已提交作业
     *
     * @param lessonGroupId
     * @param startDate
     * @param endDate
     * @param useId         学生视图时传useId
     * @return
     */
    private List<HomeworkSubmission> queryHomeworkSubmissionsByUserId(Long lessonGroupId, Long startDate, Long endDate, Long useId) {

        List<HomeworkSubmission> homeworkSubmissions;

        if (Objects.isNull(lessonGroupId)) {
            //查询指定时间段内该学生所有课程的作业
            homeworkSubmissions = homeworkSubmissionService.queryByStuIdAndTime(startDate, endDate, useId);
        } else {
            //查询指定时间段内该学生某个课程的作业
            homeworkSubmissions = homeworkSubmissionService.queryByGroupIdAndStuIdAndTime(lessonGroupId, startDate, endDate, useId);
        }
        return homeworkSubmissions;
    }


    /**
     * 已改
     * 根据课程分组、开始时间、结束时间查询所有已提交作业
     *
     * @param lessonGroupId
     * @param startDate
     * @param endDate
     * @param homeworkId    作业id
     * @return
     */
    private List<HomeworkSubmission> queryHomeworkSubmissionsByHomeworkId(Long lessonGroupId, Long startDate, Long endDate, Long homeworkId) {

        List<HomeworkSubmission> homeworkSubmissions;

        if (Objects.isNull(homeworkId)) {
            //查询某课程组的所有作业的提交记录
            homeworkSubmissions = homeworkSubmissionService.queryByGroupIdAndTime(lessonGroupId, startDate, endDate);
        } else {
            //查询某课程组某次作业的提交记录
            homeworkSubmissions = homeworkSubmissionService.queryByGroupIdAndHomeworkIdAndTime(lessonGroupId, startDate, endDate, homeworkId);
        }
        return homeworkSubmissions;
    }

    /**
     * 根据课程分组、开始时间、结束时间查询所有已提交作业，包括没打分的
     *
     * @param lessonGroupId
     * @param startDate
     * @param endDate
     * @return
     */
    private List<HomeworkSubmission> queryAllHomeworkSubmissionsByGroupId(Long lessonGroupId, Long startDate, Long endDate) {

        List<HomeworkSubmission> homeworkSubmissions = homeworkSubmissionService.queryAllByGroupIdAndTime(lessonGroupId, startDate, endDate);
        return homeworkSubmissions;
    }

    @Override
    public List<Double> teacherViewCompletePercent(Long lessonGroupId, Long homeworkId, Long startDate, Long endDate) {

        List<Double> completePercent;
        //查询当前作业左右已提交作业
        List<HomeworkSubmission> homeworkSubmissions = this.queryHomeworkSubmissionsByHomeworkId(lessonGroupId, startDate, endDate, homeworkId);
        if (CollectionUtils.isEmpty(homeworkSubmissions)) {
            completePercent = Arrays.asList(0.0, 0.0, 0.0, 0.0, 1.0);
        } else {
            completePercent = constructCompletePercent(homeworkSubmissions);
        }
        return completePercent;
    }

    @Override
    public List<Double> studentViewCompletePercent(Long lessonGroupId, Long startDate, Long endDate) {
        UserDTO userDTO = userService.currentLoginUser();
        //查询指定时间段内该学生某个课程的作业
        List<HomeworkSubmission> homeworkSubmissions = queryHomeworkSubmissionsByUserId(lessonGroupId, startDate, endDate, userDTO.getId());
        if (CollectionUtils.isEmpty(homeworkSubmissions)) {
            return Arrays.asList(0.0, 0.0, 0.0, 0.0, 1.0);
        } else {
            return constructCompletePercent(homeworkSubmissions);
        }
    }

    /**
     * 已改
     * <p>
     * 只根据已提交作业来计算成绩分布
     *
     * @param homeworkSubmissions 已提交作业
     * @return 成绩分布
     */
    private List<Double> constructCompletePercent(List<HomeworkSubmission> homeworkSubmissions) {
        //优秀
        long excellent = homeworkSubmissions.stream().filter(x -> x.getScore() >= 9).count();
        //良好
        long good = homeworkSubmissions.stream().filter(x -> (x.getScore() >= 8 && x.getScore() < 9)).count();
        //中等
        long middle = homeworkSubmissions.stream().filter(x -> (x.getScore() >= 7 && x.getScore() < 8)).count();
        //及格
        long pass = homeworkSubmissions.stream().filter(x -> x.getScore() >= 6 && x.getScore() < 7).count();
        //不及格
        long unPass = homeworkSubmissions.stream().filter(x -> x.getScore() < 6).count();
        //已提交并评分了的作业数量
        int commit = homeworkSubmissions.size();
        return Arrays.asList((double) excellent / commit, (double) good / commit,
                (double) middle / commit, (double) pass / commit, (double) unPass / commit);
    }

    @Override
    public List<DataCensusScoresDTO> queryStudentScore(Long lessonGroupId, Long startDate, Long endDate) {
        UserDTO userDTO = userService.currentLoginUser();
        List<HomeworkSubmission> homeworkSubmissions = queryHomeworkSubmissionsByUserId(lessonGroupId, startDate, endDate, userDTO.getId());
        if (CollectionUtils.isEmpty(homeworkSubmissions)) {
            return Collections.emptyList();
        }
        return homeworkSubmissions.stream().
                map(x -> new DataCensusScoresDTO(x.getHomework().getName(), x.getScore().toString()))
                .collect(Collectors.toList());
    }
}
