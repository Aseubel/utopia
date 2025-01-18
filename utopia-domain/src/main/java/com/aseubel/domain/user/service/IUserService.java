package com.aseubel.domain.user.service;

import com.aseubel.domain.user.model.UserEntity;

/**
 * @author Aseubel
 * @description 用户领域服务接口
 * @date 2025-01-12 17:27
 */
public interface IUserService {

    /**
     * 用户登录
     * @param code 登录凭证
     * @return 用户实体
     */
    UserEntity login(String code);

    /**
     * 查询用户信息
     * @param openid 用户id
     * @return 用户实体
     */
    UserEntity queryUserInfo(String openid);

    /**
     * 刷新token接口
     * @param user 用户实体
     * @return 用户实体
     */
    UserEntity refreshToken(UserEntity user);

    /**
     * 修改用户个人信息
     * @param user
     */
    void updateUserInfo(UserEntity user);

}
