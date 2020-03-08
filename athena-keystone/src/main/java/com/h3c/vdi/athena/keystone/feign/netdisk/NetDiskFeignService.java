package com.h3c.vdi.athena.keystone.feign.netdisk;

import com.h3c.vdi.athena.keystone.dto.UserDTO;
import com.h3c.vdi.athena.keystone.feign.configserver.ConfigServerFeignClient;
import com.h3c.vdi.athena.keystone.feign.model.CreateUserReq;
import com.h3c.vdi.athena.keystone.feign.model.ModifyUserReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * @author w14014
 * @date 2018/9/27
 */
@Slf4j
@Component
public class NetDiskFeignService {

    @Resource
    private NetDiskFeignClient netDiskFeignClient;

    @Resource
    private ConfigServerFeignClient configServerFeignClient;

    public void createUser(UserDTO userDTO){
        CreateUserReq createUserReq = new CreateUserReq();
        createUserReq.setUserid(userDTO.getUsername());
        createUserReq.setPassword(userDTO.getPassword());
        this.netDiskFeignClient.createUser(createUserReq);
        modifyUserAfterCreate(userDTO);
    }

    public void modifyUserAfterCreate(UserDTO userDTO){
        ModifyUserReq modifyUserReq = new ModifyUserReq();
        modifyUserReq.setDisplayname(userDTO.getName());
        //读取参数配置模块的配置
        String quota = this.configServerFeignClient.queryQuota().getQuota() + " GB";
        modifyUserReq.setQuota(quota);
        this.netDiskFeignClient.modifyUser(userDTO.getUsername(), modifyUserReq);
    }

    public void modifyUser(UserDTO userDTO){
        ModifyUserReq modifyUserReq = new ModifyUserReq();
        modifyUserReq.setPassword(userDTO.getPassword());
        modifyUserReq.setDisplayname(userDTO.getName());
        this.netDiskFeignClient.modifyUser(userDTO.getUsername(), modifyUserReq);
    }

    public void removeUser(String userId){
        this.netDiskFeignClient.removeUser(userId);
    }

    public void removeUser(List<String> userIds){

        String[] ids = userIds.toArray(new String[userIds.size()]);

        this.netDiskFeignClient.removeUser(ids);

    }

}
