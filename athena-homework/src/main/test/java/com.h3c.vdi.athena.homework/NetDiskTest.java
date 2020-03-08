package com.h3c.vdi.athena.homework;

import com.h3c.vdi.athena.homework.dao.UserGroupRelationDao;
import com.h3c.vdi.athena.homework.dto.LessonGroupDTO;
import com.h3c.vdi.athena.homework.dto.RegistrarDTO;
import com.h3c.vdi.athena.homework.dto.UserDTO;
import com.h3c.vdi.athena.homework.service.lessongroup.LessonGroupService;
import com.h3c.vdi.athena.homework.service.user.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by w14014 on 2018/9/26.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class NetDiskTest {

    @Resource
    private LessonGroupService lessonGroupService;

    @Resource
    private UserService userService;

    @Resource
    private UserGroupRelationDao userGroupRelationDao;

    @Test
    public void testTransactional(){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(20L);
        LessonGroupDTO lessonGroupDTO = new LessonGroupDTO();
        lessonGroupDTO.setUser(userDTO);
        lessonGroupDTO.setName("wwwwww");
        lessonGroupDTO.setMemberLimit(12);
        lessonGroupService.addGroup(lessonGroupDTO);
    }


    @Test
    public void testQuery(){

        List<Long> ids = userGroupRelationDao.findUserIdsNotDeletedByGroupId(2L);
        System.out.println(ids);
    }

}
