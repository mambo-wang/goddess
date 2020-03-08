package com.h3c.vdi.athena.configserver.mapper;

import com.h3c.vdi.athena.configserver.model.entity.Parameter;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 *
 * @author w14014
 * @date 2018/9/27
 */
@Mapper
public interface ParameterMapper {

    @Select("SELECT * FROM tbl_parameter")
    List<Parameter> selectAll();

    @Update("update tbl_parameter set Value=#{value} where ID=#{id}")
    void update(Parameter parameter);
}
