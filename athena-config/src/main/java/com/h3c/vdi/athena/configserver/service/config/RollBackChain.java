package com.h3c.vdi.athena.configserver.service.config;

import com.h3c.vdi.athena.common.config.SSHConfig;
import com.h3c.vdi.athena.common.utils.JsonUtils;
import com.h3c.vdi.athena.common.utils.RedisUtil;
import com.h3c.vdi.athena.common.utils.SSHExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author w14014
 * @date 2018/10/15
 */
@Component
@Slf4j
class RollBackChain {

    @Resource
    private RedisUtil redisUtil;

    private List<String> cmds = new ArrayList<>();

    void addRollBack(String rollBack){
        cmds.add(rollBack);
    }

    void clear(){
        cmds.clear();
    }

    void doRollBack() {
        if(CollectionUtils.isEmpty(cmds)){
            return;
        }
        Collections.reverse(cmds);

        SSHConfig sshConfig = JsonUtils.fromJson((String) redisUtil.get(RedisUtil.REDIS_KEY_SSHCONFIG), SSHConfig.class);

        try(SSHExecutor sshExecutor = SSHExecutor.newInstance(sshConfig)){
            cmds.forEach(cmd -> {
                try{
                    sshExecutor.exec(cmd);
                }catch (Exception e){
                    log.warn("roll back fail", e);
                }
            });
            cmds.clear();
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("SSH connect create fail", e);
        }
    }
}
