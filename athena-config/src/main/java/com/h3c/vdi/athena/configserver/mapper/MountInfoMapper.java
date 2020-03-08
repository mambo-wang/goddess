package com.h3c.vdi.athena.configserver.mapper;

import com.h3c.vdi.athena.configserver.model.entity.MountInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 *
 * @author w14014
 * @date 2018/9/28
 */
@Mapper
public interface MountInfoMapper {

    @Select("select * from tbl_mount_info")
    @Results({@Result(property = "id", column = "ID"),
            @Result(property = "fileSystemPath", column = "FILE_SYSTEM"),
            @Result(property = "mountPoint", column = "MOUNT_POINT"),
            @Result(property = "type", column = "TYPE"),
            @Result(property = "options", column = "OPTIONS"),
            @Result(property = "dump", column = "DUMP"),
            @Result(property = "pass", column = "PASS")
    })
    List<MountInfo> selectAll();
}
