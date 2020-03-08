package com.h3c.vdi.athena.homework.feign.netdisk;

import com.h3c.vdi.athena.homework.config.feign.FeignConfiguration;
import com.h3c.vdi.athena.homework.feign.model.GroupInfo;
import com.h3c.vdi.athena.homework.feign.model.ModifyGroupReq;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author w14014
 * @date 2018/9/26
 */
@FeignClient(name = "athena-netdisk", configuration = FeignConfiguration.class)
public interface NetDiskFeignClient {

    @RequestMapping(value = "/ncuser/groups", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void createGroup(@RequestBody GroupInfo groupInfo);

    @RequestMapping(value = "/ncuser/groups/{groupId}", method = RequestMethod.DELETE)
    void deleteGroup(@PathVariable(value = "groupId") String groupId);

    @RequestMapping(value = "/ncuser/groups/batch/{groupIds}", method = RequestMethod.DELETE)
    void deleteGroup(@PathVariable(value = "groupIds") String[] groupIds);

    @RequestMapping(value = "/ncuser/groups", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    void modifyGroup(@RequestBody ModifyGroupReq modifyGroupReq);

    @RequestMapping(value = "/groups/{groupId}/users/{userIds}", method = RequestMethod.POST)
    void addUsersToGroup(@PathVariable(value = "groupId")String groupId, @PathVariable(value = "userIds")String[] userIds);

    @RequestMapping(value = "/groups/{groupId}/users/{userIds}", method = RequestMethod.DELETE)
    void removeUsersFromGroup(@PathVariable(value = "groupId")String groupId, @PathVariable(value = "userIds")String[] userIds);

}




