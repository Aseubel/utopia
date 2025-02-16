package com.aseubel.domain.community.service;

import com.aliyuncs.exceptions.ClientException;
import com.aseubel.domain.community.model.bo.CommunityBO;
import com.aseubel.domain.community.model.entity.CommentEntity;
import com.aseubel.domain.community.model.entity.CommunityImage;
import com.aseubel.domain.community.model.entity.DiscussPostEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Aseubel
 * @description 社区服务接口
 * @date 2025-01-23 19:08
 */
public interface ICommunityService {

    /**
     * 获取帖子列表
     * @param postId 上一页查询最后一个帖子的id
     * @param limit 每页显示数量
     * @return 帖子列表
     */
    List<DiscussPostEntity> listDiscussPost(String userId, String postId, Integer limit, String schoolCode);

    /**
     * 上传帖子图片
     * @param postImage
     * @return
     */
    CommunityImage uploadPostImage(CommunityImage postImage) throws ClientException;

    /**
     * 发布帖子
     * @param discussPostEntity 帖子实体
     */
    void publishDiscussPost(DiscussPostEntity discussPostEntity);

    /**
     * 获取用户收藏帖子列表
     * @param userId 用户id
     * @param postId 上一页查询最后一个帖子的id
     * @param limit 每页显示数量
     * @return
     */
    List<DiscussPostEntity> queryUserFavoritePosts(String userId, String postId, Integer limit);

    /**
     * 收藏帖子
     * @param userId 用户id
     * @param postId 帖子id
     */
    void favoriteDiscussPost(String userId, String postId);

    /**
     * 置顶帖子
     * @param postId 被置顶的帖子id
     */
    void topDiscussPost(String userId, String postId);

    /**
     * 评论帖子
     * @param communityBO
     */
    @Transactional(rollbackFor = Exception.class)
    void commentDiscussPost(CommunityBO communityBO);

    /**
     * 回复评论
     * @param communityBO
     */
    @Transactional(rollbackFor = Exception.class)
    void replyComment(CommunityBO communityBO);
}
