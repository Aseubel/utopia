package com.aseubel.infrastructure.dao;

import com.aseubel.infrastructure.dao.po.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    /**
     * 通过userId获取用户信息
     * @param userId 用户id
     * @return 用户信息
     */
    User getUserByUserId(String userId);

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

}
