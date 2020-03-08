package com.h3c.vdi.athena.netdisk.mappers;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 *
 * @author w14014
 * @date 2018/10/9
 */
public interface GroupUserMapper {


    @Select("SELECT uid FROM group_user WHERE gid = #{gid}")
    List<String> getUIdsByGId(@Param("gid")String gid);
}
