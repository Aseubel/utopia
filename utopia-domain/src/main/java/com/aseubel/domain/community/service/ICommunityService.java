package com.aseubel.domain.community.service;

import com.aliyuncs.exceptions.ClientException;
import com.aseubel.domain.community.model.entity.CommentEntity;
import com.aseubel.domain.community.model.entity.CommunityImage;
import com.aseubel.domain.community.model.entity.DiscussPostEntity;

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
     * @return
     */
    List<DiscussPostEntity> listDiscussPost(String postId, Integer limit, String schoolCode);

    /**
     * 上传帖子图片
     * @param postImage
     * @return
     */
    CommunityImage uploadPostImage(CommunityImage postImage) throws ClientException;

    /**
     * 发布帖子
     * @param discussPostEntity
     */
    void publishDiscussPost(DiscussPostEntity discussPostEntity);

    /**
     * 点赞帖子
     * @param postId
     * @return
     */
    DiscussPostEntity likeDiscussPost(String postId);

    /**
     * 评论帖子
     * @param commentEntity
     * @return
     */
    DiscussPostEntity commentDiscussPost(CommentEntity commentEntity);

    /**
     * 转发帖子
     * @param postId
     * @return
     */
    DiscussPostEntity forwardDiscussPost(String postId);

    /**
     * 置顶帖子
     * @param postId
     * @return
     */
    DiscussPostEntity topDiscussPost(String postId);

}
