package com.h3c.vdi.athena.homework.feign.keystone;

import com.h3c.vdi.athena.common.model.CommonDTO;
import com.h3c.vdi.athena.homework.config.feign.FeignConfiguration;
import com.h3c.vdi.athena.homework.dto.UserDTO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JemmyZhang on 2018/2/26
 */
@FeignClient(name = "athena-keystone", configuration = FeignConfiguration.class)
public interface UserFeignService {

    @RequestMapping(value = "/users",method = RequestMethod.GET)
    List<UserDTO> finds(@RequestParam(value = "login_name") String loginName,
                       @RequestParam(value = "username") String userName,
                       @RequestParam(value = "email") String email);

    @RequestMapping(value = "/users",method = RequestMethod.POST)
    Long addUser(@RequestBody UserDTO userDTO);

    @RequestMapping(value = "/users",method = RequestMethod.GET)
    List<UserDTO> findAllDTOs(@RequestParam(value = "role_type") Integer role_type);

    @RequestMapping(value = "/users/{id}",method = RequestMethod.GET)
    UserDTO queryUserById(@PathVariable(name = "id") Long id);

    @RequestMapping(value = "/users/ids/{ids}",method = RequestMethod.GET)
    List<UserDTO> queryUserByIds(@PathVariable(name = "ids") Long[] ids);

    @RequestMapping(value = "/users",method = RequestMethod.PUT,consumes = MediaType.APPLICATION_JSON_VALUE)
    void updateUser(@RequestBody UserDTO userDTO);

    @RequestMapping(value = "/users/delete",method = {RequestMethod.PUT},consumes = MediaType.APPLICATION_JSON_VALUE)
    List<UserDTO> deleteUser(@RequestBody CommonDTO<ArrayList<Long>> commonDTO);

    @RequestMapping(value = "/users/find_by_login_name", method = RequestMethod.GET)
    UserDTO findByUsername(@RequestParam(value = "loginName") String loginName);

    @RequestMapping(value = "/users/archive/{userIds}",method = RequestMethod.PUT)
    void archiveUsers(@PathVariable(name = "userIds") List<Long> userIds);

    /**
     * 查找不在任何课程组的学生
     * @param ids
     * @return
     */
    @RequestMapping(value = "/users/usersNotInGroups/{ids}",method = RequestMethod.GET)
    List<UserDTO> queryUsersNotInGroup(@PathVariable(name = "ids") Long[] ids);

}
