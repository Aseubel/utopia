package com.aseubel.infrastructure.dao.file;

import com.aseubel.infrastructure.dao.po.Course;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author Aseubel
 * @date 2025-02-26 18:49
 */
@Mapper
public interface CourseMapper {

    @Select("SELECT id, course_name, major_name FROM course WHERE major_name = #{majorName}")
    Course listCourseByMajorName(String majorName);

    @Select("SELECT course_name FROM course WHERE major_name = #{majorName}")
    List<String> listCourseNamesByMajorName(String majorName);

    @Select("SELECT course_name FROM course " +
            "WHERE major_name in " +
            "<foreach collection='majorNames' item='majorName' open='(' separator=',' close=')'>" +
            "#{majorName}" +
            "</foreach>")
    List<String> listCourseByMajorNames(List<String> majorNames);

}
