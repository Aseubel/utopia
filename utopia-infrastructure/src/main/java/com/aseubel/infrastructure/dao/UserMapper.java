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

}
