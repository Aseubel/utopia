package com.aseubel.infrastructure.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author Aseubel
 * @date 2025-02-26 18:49
 */
@Mapper
public interface MajorMapper {

    @Select("SELECT id FROM major WHERE major_name = #{majorName}")
    Integer getMajorId(String majorName);

    @Select("SELECT major_name FROM major WHERE id = #{majorId}")
    String getMajorName(Integer majorId);

    @Select("SELECT major_name FROM major")
    List<String> getAllMajor();

}
