package com.h3c.vdi.athena.netdisk.mappers;

import com.h3c.vdi.athena.netdisk.model.entity.Share;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 *
 * @author w14014
 * @date 2018/9/26
 */
public interface ShareMapper {

    @Select("SELECT * FROM share WHERE uid_owner = #{userId}")
    @Results({@Result(property = "id", column = "id"),
            @Result(property = "shareType", column = "share_type"),
            @Result(property = "uIdOwner", column = "uid_owner"),
            @Result(property = "sTime", column = "stime"),
            @Result(property = "itemType", column = "item_type"),
            @Result(property = "fileTarget", column = "file_target"),
            @Result(property = "shareWith", column = "share_with")
    })
    List<Share> getShareListByOwnerId(@Param("userId")String userId);

    @Select("SELECT * FROM share WHERE share_with = #{userId} AND share_type=#{shareType}")
    @Results({@Result(property = "id", column = "id"),
            @Result(property = "shareType", column = "share_type"),
            @Result(property = "uIdOwner", column = "uid_owner"),
            @Result(property = "sTime", column = "stime"),
            @Result(property = "itemType", column = "item_type"),
            @Result(property = "fileTarget", column = "file_target"),
            @Result(property = "shareWith", column = "share_with")
    })
    List<Share> getShareListBySharedUserId(@Param("userId")String userId, @Param("shareType")Integer shareType);

}
