package com.aseubel.domain.community.adapter.repo;

import com.aseubel.domain.user.model.entity.UserEntity;

import java.util.List;

/**
 * @author Aseubel
 * @description 社区用户信息仓储层接口
 * @date 2025-01-25 10:26
 */
public interface ICommunityUserRepository {

    /**
     * 查询其他用户的头像和名称
     * @param userIds
     * @return
     */
    List<UserEntity> queryUserAvatarAndName(List<String> userIds);

}
