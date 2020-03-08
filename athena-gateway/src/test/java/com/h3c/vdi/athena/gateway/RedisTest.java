package com.h3c.vdi.athena.gateway;

import com.h3c.vdi.athena.common.utils.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * Created by w14014 on 2018/9/22.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {


    @Resource
    private RedisUtil util;


    @Test
    public void testRedisUtil(){
        util.set("admin","h3c.com");

        assert Objects.equals("h3c.com", (String) util.get("admin"));
    }

}
