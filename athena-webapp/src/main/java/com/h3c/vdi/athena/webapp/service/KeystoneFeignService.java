package com.h3c.vdi.athena.webapp.service;

import com.h3c.vdi.athena.common.model.CommonDTO;
import com.h3c.vdi.athena.webapp.config.feign.FeignMultipartSupportConfig;
import com.h3c.vdi.athena.webapp.dto.UserDTO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by w14014 on 2018/3/2.
 */
@FeignClient(value = "athena-gateway", configuration = FeignMultipartSupportConfig.class)
public interface KeystoneFeignService {

    @GetMapping(value = "/keystone/users/test")
    String hi(@RequestParam(value = "login_name") String loginName);

    @GetMapping(value = "/keystone/users/current_login_user")
    UserDTO currentLoginUser();

    @PutMapping(value = "/keystone/users/update_password", consumes = MediaType.APPLICATION_JSON_VALUE)
    void updatePassword(@RequestBody UserDTO userDTO);

    @PatchMapping("/keystone/users/reset_password")
    void resetPassword(@RequestParam(value = "id") Long id);

    @PutMapping(value = "/keystone/users/delete", consumes = MediaType.APPLICATION_JSON_VALUE)
    void deleteUsers(@RequestBody CommonDTO<ArrayList<Long>> commonDTO);

    @PostMapping(value = "/keystone/users", consumes = MediaType.APPLICATION_JSON_VALUE)
    Long addUser(@RequestBody UserDTO userDTO);

    @RequestMapping(value = "/keystone/users/update",method = {RequestMethod.PUT},consumes = MediaType.APPLICATION_JSON_VALUE)
    void updateUserInfo(@RequestBody UserDTO userDTO);
}
