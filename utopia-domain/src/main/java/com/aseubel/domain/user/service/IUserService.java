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

}
