package com.aseubel.infrastructure.dao;

import com.aseubel.domain.user.model.vo.School;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author Aseubel
 * @description SchoolMapper
 * @date 2025-02-05 13:57
 */
@Mapper
public interface SchoolMapper {

    /**
     * 根据学校代码获取学校信息
     * @param schoolCode
     * @return
     */
    @Select("SELECT * FROM `school` WHERE school_code = #{schoolCode}")
    School getSchoolBySchoolCode(String schoolCode);

    /**
     * 根据学校名称获取学校信息
     * @param schoolName
     * @return
     */
    @Select("SELECT * FROM `school` WHERE school_name = #{schoolName}")
    School getSchoolBySchoolName(String schoolName);

    /**
     * 根据学校代码获取学校名称
     * @param schoolCode
     * @return
     */
    @Select("SELECT school_name FROM `school` WHERE school_code = #{schoolCode}")
    String getSchoolNameBySchoolCode(String schoolCode);

    /**
     * 获取所有学校代码
     * @return
     */
    @Select("SELECT school_code FROM `school`")
    List<String> listSchoolCode();

    /**
     * 更新学校学生人数
     * @param schoolCode
     * @param studentCount
     */
    @Update("UPDATE `school` SET student_count = #{studentCount} WHERE school_code = #{schoolCode}")
    void updateSchoolStudentCount(String schoolCode, Integer studentCount);

    /**
     * 更新学校讨论帖数目
     * @param schoolCode
     * @param discussPostCount
     */
    @Update("UPDATE `school` SET discuss_post_count = #{discussPostCount} WHERE school_code = #{schoolCode}")
    void updateSchoolDiscussPostCount(String schoolCode, Integer discussPostCount);

    /**
     * 更新学校交易帖数目
     * @param schoolCode
     * @param tradePostCount
     */
    @Update("UPDATE `school` SET trade_post_count = #{tradePostCount} WHERE school_code = #{schoolCode}")
    void updateSchoolTradePostCount(String schoolCode, Integer tradePostCount);
}
