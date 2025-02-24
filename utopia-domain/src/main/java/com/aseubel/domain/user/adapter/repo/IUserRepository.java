package com.aseubel.domain.user.adapter.repo;

import com.aseubel.domain.user.model.entity.UserEntity;

/**
 * @author Aseubel
 * @description 用户领域仓储层接口
 * @date 2025-01-12 17:29
 */
public interface IUserRepository {

    /**
     * 根据用户id查询用户信息
     * @param userId 用户id
     * @return 用户信息
     */
    UserEntity queryUserInfo(String userId);

    /**
     * 根据用户id查询其他信息
     * @param userId 用户id
     * @return 用户信息
     */
    UserEntity queryOtherInfo(String userId);

    /**
     * 保存用户信息
     * @param userEntity 用户信息
     */
    void saveUserInfo(UserEntity userEntity);

    /**
     * 添加用户
     * @param userEntity
     */
    void addUser(UserEntity userEntity);

    /**
     * 生成用户token
     * @param userId 用户id
     * @param secretKey 密钥
     * @param ttl token有效期
     * @return token
     */
    String generateUserToken(String userId, String secretKey, Long ttl);

    /**
     * 记录用户token
     * @param user 用户信息
     */
    void saveUserToken(UserEntity user);

    /**
     * 清除用户token
     * @param openid 用户id
     */
    void cleanUserToken(String openid);

    /**
     * 检查refresh_token是否有效
     * @param user 用户信息
     * @return true-有效，false-无效
     */
    boolean checkRefreshToken(UserEntity user, String secretKey);

    /**
     * 更新学校学生数量
     */
    void updateSchoolStudentCount();

    /**
     * 检查学校代号是否有效
     * @param schoolCode
     * @return
     */
    boolean isSchoolCodeValid(String schoolCode);

    /**
     * 检查用户id是否有效
     * @param userId
     * @return
     */
    boolean isUserIdValid(String userId);

    /**
     * 删除用户
     * @param openid
     */
    void deleteUser(String openid);
}
