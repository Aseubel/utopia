package com.aseubel.domain.community.adapter.repo;

import com.aseubel.domain.community.model.entity.CommentEntity;
import com.aseubel.domain.community.model.entity.CommunityImage;

import java.util.List;

/**
 * @author Aseubel
 * @description 评论仓储层接口
 * @date 2025-02-09 12:22
 */
public interface ICommentRepository {

    /**
     * 获取帖子的主要评论列表
     * @param postId
     * @return
     */
    List<CommentEntity> listPostMainComment(String postId);

    /**
     * 添加新评论
     * @param commentEntity
     */
    void saveRootComment(CommentEntity commentEntity);

    /**
     * 添加回复评论
     * @param commentEntity
     */
    void saveReplyComment(CommentEntity commentEntity);

    /**
     * 将图片关联到新评论
     * @param commentId
     * @param images
     */
    void relateNewCommentImage(String commentId, List<CommunityImage> images);

    /**
     * 获取评论的图片列表
     * @param imageIds
     * @return
     */
    List<CommunityImage> listCommentImagesByImageIds(List<String> imageIds);

    /**
     * 保存评论图片
     * @param postImage
     */
    void saveCommentImage(CommunityImage postImage);
}
