package com.h3c.vdi.athena.webapp.service;

import com.h3c.vdi.athena.common.model.CommonDTO;
import com.h3c.vdi.athena.webapp.config.feign.FeignMultipartSupportConfig;
import com.h3c.vdi.athena.webapp.dto.*;
import com.h3c.vdi.athena.webapp.enums.CheckEvent;
import feign.Response;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by w14014 on 2018/3/2.
 */
@FeignClient(value = "athena-gateway", configuration = FeignMultipartSupportConfig.class)
public interface HomeworkFeignService {

    @GetMapping(value = "/homework/test")
    String test();


    /************************************用户*********************************/
    /**
     * 学生注册
     *
     * @param registrarDTO
     */
    @RequestMapping(value = "/homework/users/register",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    RegisterResultDTO studentRegister(@RequestBody RegistrarDTO registrarDTO);

    /**
     * 学生申请加入班级
     * @param userId
     * @param classId
     */
    @RequestMapping(value = "/homework/users/applyForClass", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    void applyForClass(@RequestParam(value = "userId")Long userId,@RequestParam(value = "classId")Long classId);

    @GetMapping(value = "/homework/users")
    List<UserDTO> queryUsers(@RequestParam(value = "role_type") Integer role_type);

    @GetMapping(value = "/homework/users/students")
    List<UserDTO> queryStudents();

    @GetMapping(value = "/homework/users/{id}")
    UserDTO getUserById(@PathVariable(name = "id") Long id);

    @RequestMapping(value = "/homework/users", method = {RequestMethod.PUT}, consumes = MediaType.APPLICATION_JSON_VALUE)
    void updateUser(@RequestBody UserDTO userDTO);

    /**
     * 查找当前登录用户所在班级的所有学生，如果当前登录用户不是班长则报错
     * @return
     */
    @GetMapping(value = "/homework/users/classmates")
    List<UserDTO> queryClassmates();

    /**
     * 查找当前用户待处理的注册申请
     */
    @GetMapping(value = "/homework/users/unhandledRegistrars")
    List<RegistrarDTO> queryAllUnhandledRegistrars();

    /**
     * 查找所有当前登录用户为处理人的注册申请
     */
    @GetMapping(value = "/homework/users/registrars")
    List<RegistrarDTO> queryAllRegistrars();

    /**
     * 处理注册申请
     */
    @PutMapping(value = "/homework/users/registrars/check", consumes = MediaType.APPLICATION_JSON_VALUE)
    boolean check(@RequestBody HandleUserDTO handleUserDTO);

    /**
     * 班长将某个学生从班级中踢出来
     * @param handleUserDTO
     */
    @PutMapping(value = "/homework/users/kickOut", consumes = MediaType.APPLICATION_JSON_VALUE)
    void kickOut(@RequestBody HandleUserDTO handleUserDTO);

    @PutMapping(value = "/homework/users/changeMonitor/{userId}")
    void changeMonitor(@PathVariable(value = "userId") Long userId);

    @PutMapping(value = "/homework/users/changeClass/{classId}")
    void changeClass(@PathVariable(value = "classId") Long classId);

    /**
     * 在过度界面点击我是班长，然后输入邀请码，提交申请
     * @param inviteCode
     */
    @PutMapping(value = "/homework/users/isMonitor/{inviteCode}")
    void isMonitor(@PathVariable(value = "inviteCode") Integer inviteCode);

    @GetMapping(value = "/homework/users/downloadStuTemplate", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    Response downloadStuTemplate();

    @PostMapping(value = "/homework/users/importStudents", consumes = MediaType.APPLICATION_JSON_VALUE)
    List<UserDTO> importStudents(@RequestBody List<UserDTO> userDTOS);

    /********************************** 教师作业**************************************/
    /**
     * 查询当前登录人发布的作业
     * @return
     */
    @GetMapping(value = "/homework/homeworks")
    List<HomeworkDTO> queryHomeworkDTOs();

    @GetMapping(value = "/homework/homeworks/{id}")
    HomeworkDTO queryHomeworkDTO(@PathVariable(name="id") Long id);

    /**
     * 提交作业
     * @param homeworkDTO
     */
    @PostMapping(value = "/homework/homeworks",consumes = MediaType.APPLICATION_JSON_VALUE)
    void addHomework(@RequestBody HomeworkDTO homeworkDTO);

    /**
     * 修改作业
     * @param homeworkDTO
     */
    @PutMapping(value = "/homework/homeworks",consumes = MediaType.APPLICATION_JSON_VALUE)
    void updateHomework(@RequestBody HomeworkDTO homeworkDTO);

    /**
     * 删除作业，连带删除该作业下学生提交的作业和附件
     * @param id
     */
    @PutMapping(value = "/homework/homeworks/{id}",consumes = MediaType.APPLICATION_JSON_VALUE)
    void deleteHomework(@PathVariable(name = "id") Long id);

    /**
     * 查询某作业的所有学生作业提交的状况
     * @param id
     * @return
     */
    @GetMapping(value = "/homework/homeworks/{id}/submitted_works")
    List<HomeworkSubDTO> queryAllHomeworkSubmissions(@PathVariable(name = "id") Long id);

    /**
     * 修改是否展示
     * @param showHomeworkInfoDTO
     */
    @PutMapping(value = "/homework/homeworks/submitted_works/showed",consumes = MediaType.APPLICATION_JSON_VALUE)
    void changeShowStatus(@RequestBody ShowHomeworkInfoDTO showHomeworkInfoDTO);

    /**
     * 获取某学生提交作业详情给老师批改
     * @param id
     * @return
     */
    @GetMapping(value = "/homework/homeworks/submitted_works/{id}")
    HomeworkSubDTO getHomeworkSubDTO(@PathVariable(name = "id") Long id);


    /**
     * 给作业打分
     * @param homeworkSubDTO
     */
    @PutMapping(value = "/homework/homeworks/submitted_works",consumes = MediaType.APPLICATION_JSON_VALUE)
    void setScore(@RequestBody HomeworkSubDTO homeworkSubDTO);

    /**
     * 查询某教师作业被展示的学生作业
     * @param homeworkId
     * @return
     */
    @GetMapping(value = "/homework/homeworks/showed_homeworks/{homeworkId}")
    List<HomeworkSubDTO> queryShowedHomeworkDTOs(@PathVariable(name = "homeworkId") Long homeworkId);

    /**
     * 查询某课程组下面所有教师作业
     * @param groupId
     * @return
     */
    @GetMapping(value = "/homework/homeworks/lessonGroup/{groupId}")
    List<HomeworkDTO> queryHomeworkDTOsByLessonGroupId(@PathVariable(name = "groupId") Long groupId);

    /**
     * 根据课程组id查询该课程组下面被展示的作业
     * @param id
     * @return
     */
    @GetMapping(value = "/homework/homeworks/showed_homeworks/lessonGroup/{id}")
    List<HomeworkSubDTO> queryShowedHomeworkDTOsByGroupId(@PathVariable(name = "id") Long id);

    /********************************** 学生作业**************************************/
    /**
     * 查询当前登录学生的所有作业
     * @return
     */
    @GetMapping(value = "/homework/my/homeworks")
    List<HomeworkDTO> queryAllHomeworkSubDTOs();

    /**
     * 查看作业详情，包含作业题目和已提交的内容
     * @param homeworkId
     * @return
     */
    @GetMapping(value = "/homework/my/homeworks/{homeworkId}")
    HomeworkSubDTO queryHomeworkSubDTOByHomeworkId(@PathVariable(name = "homeworkId") Long homeworkId);

    /**
     * 学生提交作业
     * @param homeworkSubDTO
     */
    @PostMapping(value = "/homework/my/homeworks",consumes = MediaType.APPLICATION_JSON_VALUE)
    void submitHomework(@RequestBody HomeworkSubDTO homeworkSubDTO);

    /**
     * 学生修改作业
     * @param homeworkSubDTO
     */
    @PutMapping(value = "/homework/my/homeworks",consumes = MediaType.APPLICATION_JSON_VALUE)
    void updateHomeworkSubmission(@RequestBody HomeworkSubDTO homeworkSubDTO);


    /********************************** 作业模板**************************************/
    @GetMapping(value = "/homework/templates/all")
    List<HomeworkTemplateDTO> queryAllTemplates();

    @GetMapping(value = "/homework/templates")
    List<HomeworkTemplateDTO> queryByLoginUser();

    @GetMapping(value = "/homework/templates/{id}")
    HomeworkTemplateDTO queryTemplateById(@PathVariable(value = "id") Long id);

    @RequestMapping(value = "/homework/templates", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<HomeworkTemplateDTO> addTemplate(@RequestBody HomeworkTemplateDTO dto);

    @RequestMapping(value = "/homework/templates", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    void modifyTemplate(@RequestBody HomeworkTemplateDTO dto);

    @PutMapping(value = "/homework/templates/delete", consumes = MediaType.APPLICATION_JSON_VALUE)
    void deleteTemplate(@RequestBody CommonDTO<ArrayList<Long>> commonDTO);

    /********************************** 班级**************************************/
    @RequestMapping(value = "/homework/classEntity", method = RequestMethod.GET)
    List<ClassEntityDTO> findAllClasses();

    @RequestMapping(value = "/homework/classEntity/college/{collegeId}", method = RequestMethod.GET)
    List<ClassEntityDTO> findAllClassesByCollegeId(@PathVariable(name = "collegeId") Long collegeId);

    @RequestMapping(value = "/homework/classEntity", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Integer addClassEntity(@RequestBody ClassEntityDTO classEntityDTO);

    @RequestMapping(value = "/homework/classEntity", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    void updateClassEntity(@RequestBody ClassEntityDTO classEntityDTO);

    @PutMapping(value = "/homework/classEntity/delete", consumes = MediaType.APPLICATION_JSON_VALUE)
    void deleteClassEntityBatch(@RequestBody CommonDTO<ArrayList<Long>> commonDTO);

    @RequestMapping(value = "/homework/classEntity/resetInviteCode/{classId}",method = RequestMethod.PUT)
    Integer resetInviteCode(@PathVariable(name = "classId") Long classId);

    /********************************** 学院**************************************/
    @GetMapping(value = "/homework/colleges/{id}")
    CollegeDTO findCollegeById(@PathVariable(name = "id") Long id);

    @RequestMapping(value = "/homework/colleges", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void addCollege(@RequestBody CollegeDTO collegeDTO);

    @RequestMapping(value = "/homework/colleges", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    void updateCollege(@RequestBody CollegeDTO collegeDTO);

    @GetMapping(value = "/homework/colleges")
    List<CollegeDTO> queryAllColleges();

    @RequestMapping(value = "/homework/colleges/delete", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    void deleteColleges(@RequestBody CommonDTO<ArrayList<Long>> commonDTO);

    /***********************************************课程组****************************************************************/
    @RequestMapping(value = "/homework/groups/current_user", method = RequestMethod.GET)
    List<LessonGroupDTO> queryGroupsByCurrentUser();

    @RequestMapping(value = "/homework/groups/current_user/with_users", method = RequestMethod.GET)
    List<LessonGroupDTO> queryGroupsByCurrentUserWithUsers();

    @RequestMapping(value = "/homework/groups", method = RequestMethod.GET)
    List<LessonGroupDTO> queryAllLessonGroups();

    @RequestMapping(value = "/homework/groups/{id}", method = RequestMethod.GET)
    LessonGroupDTO getGroupById(@PathVariable(name = "id") Long id);

    @RequestMapping(value = "/homework/groups", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<LessonGroupDTO> addLessonGroup(@RequestBody LessonGroupDTO lessonGroupDTO);

    @RequestMapping(value = "/homework/groups", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<LessonGroupDTO> modifyLessonGroup(@RequestBody LessonGroupDTO lessonGroupDTO);

    @RequestMapping(value = "/homework/groups/{ids}", method = RequestMethod.DELETE)
    List<LessonGroupDTO> removeLessonGroups(@PathVariable(name = "ids") Long[] ids);

    @RequestMapping(value = "/homework/groups/{lesson_group_id}/users/{user_ids}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void addUserToGroup(@PathVariable(name = "lesson_group_id") Long lessonGroupId, @PathVariable(name = "user_ids")Long[] userIds);

    @RequestMapping(value = "/homework/groups/{lesson_group_id}/users/{user_ids}", method = RequestMethod.DELETE)
    void removeUserOutGroup(@PathVariable(name = "lesson_group_id")Long lessonGroupId, @PathVariable(name = "user_ids")Long[] userIds);

    @RequestMapping(value = "/homework/groups/{lesson_group_id}/users", method = RequestMethod.GET)
    List<UserDTO> queryUsersByGroup(@PathVariable(name = "lesson_group_id")Long lessonGroupId);

    @RequestMapping(value = "/homework/groups/my/{lesson_group_ids}", method = RequestMethod.POST)
    void applyIntoGroup(@PathVariable(name = "lesson_group_ids") Long[] lessonGroupIds);

    @RequestMapping(value = "/homework/groups/my/{lesson_group_ids}", method = RequestMethod.DELETE)
    void quitOutGroup(@PathVariable(name = "lesson_group_ids") Long[] lessonGroupIds);

    @RequestMapping(value = "/homework/groups/my/not_in", method = RequestMethod.GET)
    List<LessonGroupDTO> queryNotInGroupsByCurrentUser();

    @GetMapping(value = "/homework/groups/apply/unchecked")
    List<UserGroupRegistrarDTO> queryUnhandled();

    @GetMapping(value = "/homework/groups/apply")
    List<UserGroupRegistrarDTO> queryAllApply();

    @PutMapping(value = "/homework/groups/apply/{ids}/check/{event}")
    CommonDTO<Boolean> check(@PathVariable(value = "ids") Long[] ids,@PathVariable(value = "event") CheckEvent event);

    /**********************评论*******************/
    @RequestMapping(value = "/homework/comments",method = RequestMethod.POST)
    void addComment(@RequestBody CommentDTO commentDTO);

    @RequestMapping(value = "/homework/comments/{id}", method = RequestMethod.DELETE)
    void deleteComment(@PathVariable(name = "id") Long id);

    @RequestMapping(value = "/homework/comments/{id}/{type}", method = RequestMethod.GET)
    List<CommentDTO> getCommentByRelationId(@PathVariable(name = "id") Long relationId, @PathVariable(name = "type") int type);

    /**********************点赞*******************/
    @RequestMapping(value = "/homework/homeworkPraise/{homeworkSubmitId}/{userId}", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    void addPraise(@PathVariable(name = "homeworkSubmitId") Long homeworkSubmitId, @PathVariable(name = "userId") Long userId);

    @RequestMapping(value = "/homework/homeworkPraise/{homeworkPraiseId}/{userId}", method = RequestMethod.DELETE)
    void deletePraise(@PathVariable(name = "homeworkPraiseId") Long homeworkPraiseId,@PathVariable(name = "userId") Long userId);

    @RequestMapping(value = "/homework/homeworkPraise/{homeworkPraiseId}", method = RequestMethod.GET)
    List<HomeworkPraiseDTO> queryPraisesByHomeworkId(@PathVariable(name = "homeworkPraiseId") Long homeworkPraiseId);

    /**********************数据归档******************/
    @PutMapping(value = "/homework/archive/{year}")
    void archive(@PathVariable(name = "year") String year);

    @GetMapping(value = "/homework/archive/years")
    List<String> getYears();
}
