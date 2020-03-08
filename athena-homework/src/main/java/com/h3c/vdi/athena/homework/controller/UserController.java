package com.h3c.vdi.athena.homework.controller;

import com.h3c.vdi.athena.common.exception.AppException;

import com.h3c.vdi.athena.common.utils.StringManager;
import com.h3c.vdi.athena.homework.dto.HandleUserDTO;
import com.h3c.vdi.athena.homework.dto.RegisterResultDTO;
import com.h3c.vdi.athena.homework.dto.RegistrarDTO;
import com.h3c.vdi.athena.homework.dto.UserDTO;
import com.h3c.vdi.athena.homework.service.registrar.RegistrarService;
import com.h3c.vdi.athena.homework.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * Created by w16051 on 2018/3/2.
 */
@RestController
@RequestMapping(value = "/users")
public class UserController {
    @Resource
    UserService userService;

    @Resource
    RegistrarService registrarService;

    private static StringManager sm = StringManager.getManager("User");

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    /**
     * 学生注册
     * 返回注册结果，如果失败，结果包含失败原因，如果待班长批准，结果包含班长名字
     *
     * @param registrarDTO
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public RegisterResultDTO studentRegister(@RequestBody RegistrarDTO registrarDTO) {
        RegisterResultDTO result = new RegisterResultDTO();
        try {
            result = userService.submitRegisterRequest(registrarDTO);
            logger.info("add " + registrarDTO.getUsername() + " success");
        } catch (AppException ae) {
            logger.warn("add " + registrarDTO.getUsername() + " failure", ae);
        } catch (Exception e) {
            logger.warn("add " + registrarDTO.getUsername() + " failure", e);
        }
        return result;
    }

    /**
     * 申请加入班级
     * @param userId
     * @param classId
     */
    @RequestMapping(value = "/applyForClass", method = RequestMethod.POST)
    public void applyForClass(@RequestParam(value = "userId")Long userId,@RequestParam(value = "classId")Long classId){
        userService.applyForClass(userId,classId);
    }

    /**
     * 查询用户.
     *
     * @return userDTOList
     */
    @GetMapping()
    public List<UserDTO> queryUsers(@RequestParam(value = "role_type") Integer role_type) {
        List<UserDTO> userDTOList;
        try {
            userDTOList = userService.findAllDTOs(role_type);
            logger.info("query users success");
            return userDTOList;
        } catch (AppException ae) {
            logger.warn("query users failure", ae);
        } catch (Exception e) {
            logger.warn("query users failure", e);
        }
        return null;
    }

    @GetMapping(value = "/students")
    public List<UserDTO> queryStudentInfo() {
        List<UserDTO> userDTOList = userService.findAllStudents();
        logger.info("query users success");
        return userDTOList;
    }

    /**
     * 根据id查询用户.
     *
     * @param id 用户id
     * @return user对象
     */
    @RequestMapping(value = {"/{id}"}, method = {RequestMethod.GET})
    public UserDTO getUserById(@PathVariable Long id) {

        UserDTO userDTO = new UserDTO();
        try {
            userDTO = userService.queryUserById(id);
            logger.info("query " + userDTO.getUsername() + " success");
            return userDTO;
        } catch (AppException ae) {
            logger.warn("query " + userDTO.getUsername() + " failure", ae);
        } catch (Exception e) {
            logger.warn("query " + userDTO.getUsername() + " failure", e);
        }
        return null;
    }

    /**
     * 修改用户.
     *
     * @param userDTO user对象
     */
    @RequestMapping(value = "", method = {RequestMethod.PUT})
    public void updateUser(@RequestBody UserDTO userDTO) {
        try {
            userService.updateUser(userDTO);
            logger.info("update " + userDTO.getUsername() + " success");
        } catch (AppException e) {
            logger.warn("update " + userDTO.getUsername() + " failure", e);
            logger.warn("update " + userDTO.getUsername() + " failure", e);
        } catch (Exception e) {
            logger.warn("update " + userDTO.getUsername() + " failure", e);
        }
    }


    /**
     * 删除用户相关信息：
     * 学生账户移出班级和课程组，删除皮肤配置信息,删除提交作业和附件
     * 教师删除发布作业和附件
     * @param
     * @return userList
     */
    @RequestMapping(value = "/delete/{userId}/role/{roleId}",method = RequestMethod.PUT)
    public void deleteUser(@PathVariable(value = "userId") Long userId,@PathVariable(value = "roleId") Long roleId){
        userService.deleteUser(userId,roleId);
    }

    /**
     * 查找当前用户待处理的注册申请
     * @return
     */
    @GetMapping(value = "/unhandledRegistrars")
    public List<RegistrarDTO> queryAllUnhandledRegistrars(){
        return registrarService.queryAllUnhandledRegistrars();
    }

    /**
     * 查找所有当前登录用户为处理人的注册申请
     * @return
     */
    @GetMapping(value = "/registrars")
    public List<RegistrarDTO> queryAllRegistrars(){
        return registrarService.queryAllRegistrars();
    }

    /**
     * 处理注册申请
     * @param
     */
    @PutMapping(value = "/registrars/check")
    public boolean check(@RequestBody HandleUserDTO handleUserDTO) {
        return registrarService.check(handleUserDTO);
    }

    /**
     * 查找当前登录用户所在班级的所有学生，如果当前登录用户不是班长则报错
     * @return
     */
    @GetMapping(value = "/classmates")
    public List<UserDTO> queryClassmates(){
        return userService.queryClassmates();
    }

    /**
     * 班长将某个学生从班级中踢出来
     * @param handleUserDTO
     */
    @PutMapping(value = "/kickOut")
    public void kickOut(@RequestBody HandleUserDTO handleUserDTO){
        userService.kickOut(handleUserDTO);
    }

    /**
     * 转让班长
     * @param userId
     */
    @PutMapping(value = "/changeMonitor/{userId}")
    public void changeMonitor(@PathVariable Long userId){
        userService.changeMonitor(userId);
    }
    /**
     * 根据username查询用户
     * @param userName
     * @return
     */
    @GetMapping(value = "/userName/{userName}")
    public UserDTO queryUserByUserName(@PathVariable String userName){
        return userService.queryUserByUserName(userName);
    }

    /**
     * 变更申请
     * @param classId
     */
    @PutMapping(value = "/changeClass/{classId}")
    public void changeClass(@PathVariable(value = "classId") Long classId){
        userService.changeClass(classId);
    }

    /**
     * 在过度界面点击我是班长，然后输入邀请码，提交申请
     * @param inviteCode
     */
    @PutMapping(value = "/isMonitor/{inviteCode}")
    public void isMonitor(@PathVariable(value = "inviteCode") Integer inviteCode){
        userService.isMonitor(inviteCode);
    }

    /**
     * 上传学生批量导入的Excel文件，返回根据文件解析出的学生列表
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping(value = "/uploadTemplate",produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<UserDTO> importStuFile(@RequestPart(name = "file") MultipartFile file) throws IOException{
        return userService.importStuFile(file);
    }

    /**
     * 导入学生
     * @param userDTOS
     * @return
     */
    @PostMapping(value = "/importStudents")
    public List<UserDTO> importStudents(@RequestBody List<UserDTO> userDTOS){
        return userService.importStudents(userDTOS);
    }

}
