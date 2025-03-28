package com.aseubel.domain.user.adapter.repo;

/**
 * @author Aseubel
 * @description 管理员仓储层接口
 * @date 2025-03-28 18:11
 */
public interface IAdminRepository {

    /**
     * 判断用户是否是管理员
     * @param userId 用户id
     * @param schoolCode 学校代码
     * @return true：是管理员，false：不是管理员
     */
    boolean isAdmin(String userId, String schoolCode);

    /**
     * 判断用户是否是管理员
     * @param userId 用户id
     * @return true：是管理员，false：不是管理员
     */
    boolean isAdmin(String userId);
}
