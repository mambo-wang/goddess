package com.h3c.vdi.athena.keystone.controller;

import com.h3c.vdi.athena.common.exception.AppException;
import com.h3c.vdi.athena.common.model.CommonDTO;
import com.h3c.vdi.athena.common.model.DefaultUserDetails;
import com.h3c.vdi.athena.common.utils.StringManager;
import com.h3c.vdi.athena.common.utils.Utils;
import com.h3c.vdi.athena.keystone.dto.SecureUserDTO;
import com.h3c.vdi.athena.keystone.dto.UserDTO;
import com.h3c.vdi.athena.keystone.service.user.UserService;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by JemmyZhang on 2018/2/24
 */
@RestController
@RequestMapping(value = "/users")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    private StringManager sm = StringManager.getManager("User");
    @Resource
    UserService userService;

    /**
     * @GetMapping public List<UserDTO> find(@RequestParam(value = "login_name", required = false) String loginName,
     * @RequestParam(value = "username", required = false) String userName,
     * @RequestParam(value = "email", required = false) String email) {
     * return userService.findDTOs(loginName, userName, email);
     * }
     */
    @GetMapping("/secure")
    public SecureUserDTO findSecureUserByName(@RequestParam(value = "login_name") String loginName) {
        return userService.findUserDetailsByLoginName(loginName);
    }

    @GetMapping("/test")
    public String hi(@RequestParam(value = "login_name") String loginName) {
        Utils.getLoginUser();
        return "hi" + loginName;
    }

    @GetMapping("/current_login_user")
    public UserDTO currentLoginUser() {
        DefaultUserDetails defaultUserDetails = Utils.getLoginUser();
        return userService.findUserDTOByName(defaultUserDetails.getUsername());
    }


    /**
     * 增加用户
     *
     * @param userDTO
     */
    @PostMapping
    public Long addUser(@RequestBody UserDTO userDTO) {
        return userService.addUser(userDTO);
    }


    /**
     * 根据id查询用户.
     *
     * @param id 用户id
     * @return user对象
     */
    @RequestMapping(value = {"/{id}"}, method = {RequestMethod.GET})
    public UserDTO getUserById(@PathVariable Long id) {
        return userService.queryUserById(id);
    }

    /**
     * 根据id查询用户.
     *
     * @param ids 用户ids
     * @return user对象
     */
    @RequestMapping(value = {"/ids/{ids}"}, method = {RequestMethod.GET})
    public List<UserDTO> getUserByIds(@PathVariable(name = "ids") Long[] ids) {
        List<UserDTO> users = userService.queryUserByIds(Arrays.asList(ids));
        return users;
    }

    /**
     * 修改用户.
     *
     * @param userDTO user对象
     */
    @RequestMapping(method = {RequestMethod.PUT})
    public void updateUser(@RequestBody UserDTO userDTO) {
        try {
            userService.updateUser(userDTO);
            logger.info("update " + userDTO.getUsername() + " success");
        } catch (AppException ae) {
            logger.warn("update " + userDTO.getUsername() + " failure", ae);

        } catch (Exception e) {
            logger.warn("update " + userDTO.getUsername() + " failure", e);
        }
    }

    @RequestMapping(value = "/keystone/users/update",method = {RequestMethod.PUT})
    public void updateUserInfo(@RequestBody UserDTO userDTO){
        userService.updateUserInfo(userDTO);
    }

    @RequestMapping(value = "/archive/{userIds}",method = RequestMethod.PUT)
    public void archiveUsers(@PathVariable List<Long> userIds){
        userService.archiveUsers(userIds);
    }


    /**
     * 删除/批量删除用户.
     *
     * @return userList
     */
    @RequestMapping(value = "delete", method = {RequestMethod.PUT})
    public void deleteUsers(@RequestBody CommonDTO<ArrayList<Long>> commonDTO) {
        ArrayList<Long> idList = commonDTO.getData();
        if (CollectionUtils.isEmpty(idList)) {
            throw new AppException(sm.getString("select.delete.user"));
        }
        userService.deleteUsers(idList);
    }

    /**
     * 查找不在任何课程组的学生
     * @param ids
     * @return
     */
    @RequestMapping(value = "/usersNotInGroups/{ids}",method = RequestMethod.GET)
    public List<UserDTO> queryUsersNotInGroup(@PathVariable(name = "ids") Long[] ids ){
        return userService.findUsersNotInGroups(ids);
    }


    /**
     * 查询用户.
     *
     * @return userDTOList
     */
    @RequestMapping(method = {RequestMethod.GET})
    @ResponseBody
    public List<UserDTO> queryUsers(@RequestParam(value = "role_type") Integer role_type) {
        List<UserDTO> userDTOList;
        try {
            userDTOList = userService.findAllDTOs(role_type);
            logger.info("query users success");
        } catch (AppException ae) {
            logger.warn("query users failure", ae);
            // throw new HttpConflictException(ae.getErrorMessage());
            return null;
        } catch (Exception e) {
            logger.warn("query users failure", e);
            // throw new HttpConflictException(ErrorCodes.getErrorMessage(ErrorCodes.VDI_NOT_FOUND_OR_NOT_RUN));
            return null;
        }
        return userDTOList;
    }

    @GetMapping("/find_by_login_name")
    public UserDTO findByLoginName(@RequestParam(value = "loginName") String loginName){
        try {
            return userService.convertUserToUserDTO(userService.findUserByLoginName(loginName));
        } catch (Exception e) {
            logger.warn("find user by loginName fail.", e);
            return null;
            // throw
        }
    }

    @PutMapping("/update_password")
    public void updatePassword(@RequestBody UserDTO userDTO) {
        userService.updatePassword(userDTO);
    }

    @PatchMapping("/reset_password")
    public void resetPassword(@RequestParam(value = "id") Long id) {
        userService.resetPassword(id);
    }


}
