package com.aseubel.domain.community.adapter.repo;

import com.aseubel.domain.user.model.entity.UserEntity;

import java.util.List;
import java.util.Map;

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
    Map<String, UserEntity> queryUserBaseInfo(List<String> userIds);

    /**
     * 查询用户状态，是否封禁之类的
     * @param userId
     * @return
     */
    UserEntity queryUserStatus(String userId);

    /**
     * 查询用户昵称
     * @param userIds
     * @return
     */
    Map<String, String> queryUserNames(List<String> userIds);

    /**
     * 查询评论的用户昵称
     * @param commentIds
     * @return 评论id和用户昵称的映射
     */
    Map<String, String> queryUserNamesByCommentIds(List<String> commentIds);
}
