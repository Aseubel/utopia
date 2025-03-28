package com.aseubel.infrastructure.dao;

import com.aseubel.infrastructure.dao.po.Admin;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author Aseubel
 * @date 2025-03-28 17:44
 */
@Mapper
public interface AdminMapper {

    /**
     * 查询所有管理员
     *
     * @return 管理员列表
     */
    @Select("SELECT * FROM admin WHERE is_deleted = 0")
    List<Admin> selectAllAdmins();

    /**
     * 根据用户ID查询管理员
     *
     * @param userId 用户ID
     * @return 管理员信息
     */
    @Select("SELECT * FROM admin WHERE user_id = #{userId} AND is_deleted = 0")
    Admin selectAdminByUserId(@Param("userId") String userId);

    /**
     * 根据校区代号查询普通管理员
     *
     * @param schoolCode 校区代号
     * @return 普通管理员列表
     */
    @Select("SELECT * FROM admin WHERE school_code = #{schoolCode} AND admin_type = 1 AND is_deleted = 0")
    List<Admin> selectCommonAdminsBySchoolCode(@Param("schoolCode") String schoolCode);

    /**
     * 查询超级管理员
     *
     * @return 超级管理员列表
     */
    @Select("SELECT * FROM admin WHERE admin_type = 0 AND is_deleted = 0")
    List<Admin> selectSuperAdmins();

    /**
     * 分页查询管理员
     *
     * @param offset 偏移量
     * @param limit  每页大小
     * @return 分页后的管理员列表
     */
    @Select("SELECT * FROM admin WHERE is_deleted = 0 LIMIT #{offset}, #{limit}")
    List<Admin> selectAdminPage(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * 判断一个用户是否为超级管理员
     *
     * @return 存在返回正数，不存在返回0
     */
    @Select("SELECT COUNT(*) FROM admin WHERE user_id = #{userId} AND admin_type = 0 AND is_deleted = 0")
    int isSuperAdmin(@Param("userId") String userId);

    /**
     * 判断一个用户是否为管理员
     *
     * @param userId 用户ID
     * @return 存在返回正数，不存在返回0
     */
    @Select("SELECT COUNT(*) FROM admin WHERE user_id = #{userId} AND is_deleted = 0")
    int isAdmin(@Param("userId") String userId);

    /**
     * 判断是否为某校区管理员
     * @param userId 用户ID
     * @param schoolCode 校区代号
     * @return 存在返回正数，不存在返回0
     */
    @Select("SELECT COUNT(*) FROM admin WHERE user_id = #{userId} " +
            "AND (school_code = #{schoolCode} OR school_code IS NULL) AND is_deleted = 0")
    int isSchoolAdmin(@Param("userId") String userId, String schoolCode);

    /**
     * 添加管理员
     *
     * @param admin 管理员对象
     * @return 影响行数
     */
    @Insert("INSERT INTO admin(user_id, admin_type, school_code) " +
            "VALUES (#{admin.userId}, #{admin.adminType}, #{admin.schoolCode})")
    int insertAdmin(@Param("admin") Admin admin);

    /**
     * 更新管理员信息
     *
     * @param admin 管理员对象
     * @return 影响行数
     */
    @Update("UPDATE admin SET admin_type = #{admin.adminType}, school_code = #{admin.schoolCode} " +
            "WHERE user_id = #{admin.userId}")
    int updateAdmin(@Param("admin") Admin admin);

    /**
     * 删除管理员
     *
     * @param userId 用户ID
     * @return 影响行数
     */
    @Update("UPDATE admin SET is_deleted = 1 WHERE user_id = #{userId}")
    int deleteAdminByUserId(@Param("userId") String userId);

}
