package com.aseubel.infrastructure.dao;

import com.aseubel.infrastructure.dao.po.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {

    /**
     * 通过userId获取用户信息
     * @param userId 用户id
     * @return 用户信息
     */
    User getUserByUserId(@Param("userId") String userId);

    /**
     * 添加用户
     * @param user 用户信息
     */
    void addUser(User user);

    /**
     * 更新用户信息
     * @param user 用户信息
     */
    void updateUser(User user);

    /**
     * 在帖子、评论板块、点赞板块等地方显示用户的头像和昵称
     * @param userIds
     * @return
     */
    List<User> listUserBaseInfoByUserIds(List<String> userIds);

    /**
     * 根据用户id获取用户状态
     * @param userId
     * @return
     */
    User getUserStatusByUserId(String userId);

    /**
     * 根据学校代号获取学生数量
     * @param schoolCode
     * @return
     */
    Integer CountStudentBySchoolCode(String schoolCode);
}
