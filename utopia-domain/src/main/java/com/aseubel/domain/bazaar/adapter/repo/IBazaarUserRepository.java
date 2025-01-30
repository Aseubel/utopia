package com.aseubel.domain.bazaar.adapter.repo;

import com.aseubel.domain.user.model.entity.UserEntity;

import java.util.List;

/**
 * @author Aseubel
 * @description 集市用户信息仓储层接口
 * @date 2025-01-25 10:26
 */
public interface IBazaarUserRepository {

    /**
     * 查询其他用户的头像和名称
     * @param userIds
     * @return
     */
    List<UserEntity> queryUserAvatarAndName(List<String> userIds);

    /**
     * 查询用户状态，是否封禁之类的
     * @param userId
     * @return
     */
    UserEntity queryUserStatus(String userId);

}
