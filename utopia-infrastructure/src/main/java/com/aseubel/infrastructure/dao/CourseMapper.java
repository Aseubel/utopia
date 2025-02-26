package com.aseubel.infrastructure.dao;

import com.aseubel.infrastructure.dao.po.Course;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author Aseubel
 * @date 2025-02-26 18:49
 */
@Mapper
public interface CourseMapper {

    @Select("SELECT id, course_name, major_name FROM course WHERE major_name = #{majorName}")
    Course listCourseByMajorName(String majorName);

}
