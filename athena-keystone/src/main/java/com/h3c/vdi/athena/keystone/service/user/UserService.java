package com.h3c.vdi.athena.keystone.service.user;

import com.h3c.vdi.athena.keystone.dto.SecureUserDTO;
import com.h3c.vdi.athena.keystone.dto.UserDTO;
import com.h3c.vdi.athena.keystone.entity.User;

import java.util.List;

/**
 * Created by JemmyZhang on 2018/2/13
 */
public interface UserService {

    List<User> find(String loginName, String userName, String email);

    List<UserDTO> findDTOs(String loginName, String userName, String email);

    List<User> findAll();

    List<UserDTO> findAllDTOs(Integer role_type);

    User findUserByLoginName(String loginName);

    UserDTO findUserDTOByName(String loginName);

    List<UserDTO> convertToDTOs(List<User> all);

    UserDTO convertUserToUserDTO(User user);

    SecureUserDTO findUserDetailsByLoginName(String loginName);
    UserDTO findLocalUserById(Long id);

    /**
     * 添加用户
     * @param userDTO
     */
    Long addUser(UserDTO userDTO);


    /**
     * 根据id查询教师
     *
     * @param id 教师id
     * @return 教师
     */
    UserDTO queryUserById(Long id);

    /**
     * 修改教师
     *
     * @param userDTO 教师
     */
    void updateUser(UserDTO userDTO);

    /**
     * 修改用户信息，只有手机号和邮箱
     * @param userDTO
     */
    void updateUserInfo(UserDTO userDTO);

    /**
     * 批量归档账号
     * @param userIds
     */
    void archiveUsers(List<Long> userIds);

    /**
     * 删除用户逻辑
     *
     * @param idList
     */
    void deleteUsers(List<Long> idList);

    /**
     * 查询不在任何课程组中的学生
     * @param userIds
     * @return
     */
    List<UserDTO> findUsersNotInGroups(Long[] userIds);

    /**
     * 查找当前用户
     */
    User currentLoginUser();

    void resetPassword(Long id);

    void updatePassword(UserDTO userDTO);

    List<UserDTO> queryUserByIds(List<Long> ids);
}
