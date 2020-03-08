package com.h3c.vdi.athena.homework.service.user;

import com.h3c.vdi.athena.common.model.CommonDTO;
import com.h3c.vdi.athena.homework.dto.HandleUserDTO;
import com.h3c.vdi.athena.homework.dto.RegisterResultDTO;
import com.h3c.vdi.athena.homework.dto.RegistrarDTO;
import com.h3c.vdi.athena.homework.dto.UserDTO;
import com.h3c.vdi.athena.homework.entity.ClassEntity;
import com.h3c.vdi.athena.homework.entity.Registrar;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by w16051 on 2018/3/2.
 */
public interface UserService {
    /**
     * 学生注册
     */
    RegisterResultDTO submitRegisterRequest(RegistrarDTO registrarDTO);

    void addStudentToClass(Registrar registrar);


    /**
     * 申请加入某班级
     * @param userId
     * @param classId
     */
    void applyForClass(Long userId,Long classId);

    /**
     * 查询所有用户
     */
    List<UserDTO> findAllDTOs(Integer role_type);

    /**
     * 查询所有学生用户
     */
    List<UserDTO> findAllStudents();

    /**
     * 根据id查询用户
     *
     * @param id 用户id
     * @return 用户
     */
    UserDTO queryUserById(Long id);

    /**
     * 根据username查询用户
     * @param username
     * @return
     */
    UserDTO queryUserByUserName(String username);

    /**
     * 修改教师
     *
     * @param userDTO 教师
     */
    void updateUser(UserDTO userDTO);

    /**
     * 删除用户相关信息：
     * 学生账户移出班级和课程组，删除皮肤配置信息,删除提交作业和附件
     * 教师删除发布作业和附件
     * @param userId
     * @param roleId
     * @return
     */
    void deleteUser(Long userId, Long roleId);

    /**
     * 当前登录用户
     * @return 用户DTO
     */
    UserDTO currentLoginUser();

    /**
     * 获取用户最高角色
     * @param userDTO  用户
     * @return 最高角色
     */
    String getRoleName(UserDTO userDTO);

    /**
     * 查找当前登录用户所在班级的所有学生，如果当前登录用户不是班长则报错
     * @return
     */
    List<UserDTO> queryClassmates();

    /**
     * 班长将某个学生从班级中踢出来,学生账号还在，但不在当前班级中
     * @param handleUserDTO
     */
    void kickOut(HandleUserDTO handleUserDTO);

    /**
     * 更换班长
     * @param userId
     */
    void changeMonitor(Long userId);

    /**
     * 变更申请
     * @param classId
     */
    void changeClass(Long classId);

    /**
     * 我是班长
     * @param inviteCode
     */
    void isMonitor(Integer inviteCode);

    /**
     * 导出excel模板
     * @return 表格
     */
//    Workbook createWorkbookMould();

    List<UserDTO> importStuFile(MultipartFile file) throws IOException;

    List<UserDTO> importStudents(List<UserDTO> userDTOS);
}
