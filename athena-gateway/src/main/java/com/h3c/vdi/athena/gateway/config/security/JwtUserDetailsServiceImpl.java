package com.h3c.vdi.athena.gateway.config.security;

import com.h3c.vdi.athena.common.model.DefaultUserDetails;
import com.h3c.vdi.athena.gateway.dto.SecureUserDTO;
import com.h3c.vdi.athena.gateway.dto.UserDTO;
import com.h3c.vdi.athena.gateway.feign.HomeworkService;
import com.h3c.vdi.athena.gateway.feign.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * Created by JemmyZhang on 2018/3/1
 */

@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService{

    @Resource
    private UserService userService;

    @Resource
    private HomeworkService homeworkService;

    /**
     * 获取用户信息
     * @param username 用户登录名
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SecureUserDTO user = userService.findUserByLoginName(username);
        validateUser(user);
        return new DefaultUserDetails(user.getUsername(), user.getPassword(), user.getRoles());
    }

    /**
     * 检验用户信息
     * @param user 用户
     */
    private void validateUser(SecureUserDTO user) {
        if(Objects.isNull(user) || Objects.isNull(user.getUsername()) || Objects.isNull(user.getPassword()) || Objects.isNull(user.getRoles())){
            throw new UsernameNotFoundException("User details not found!");
        }
    }

    public UserDTO findUserDTOByUsername(String username){
        return this.homeworkService.findUserDTOByLoginName(username);
    }

}
