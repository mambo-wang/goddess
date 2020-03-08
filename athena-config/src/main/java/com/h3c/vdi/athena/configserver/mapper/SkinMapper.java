package com.h3c.vdi.athena.configserver.mapper;

import com.h3c.vdi.athena.configserver.model.entity.Skin;
import org.apache.ibatis.annotations.*;

/**
 *
 * @author w14014
 * @date 2018/10/13
 */
@Mapper
public interface SkinMapper {

    @Select("select * from tbl_skin_config where USERNAME=#{username}")
    @Results({@Result(property = "username", column = "USERNAME"),
            @Result(property = "skinNumber", column = "SKIN_NUMBER")
    })
    Skin getByUsername(@Param("username")String username);

    @Insert("insert into tbl_skin_config values(#{username}, #{skinNumber})")
    void insert(@Param("username")String username,@Param("skinNumber")Integer skinNumber);

    @Update("update tbl_skin_config set SKIN_NUMBER=#{skinNumber} where USERNAME=#{username}")
    void update(@Param("username")String username,@Param("skinNumber")Integer skinNumber);

    @Delete("delete from tbl_skin_config where USERNAME=#{username}")
    void delete(@Param("username")String username);

}
