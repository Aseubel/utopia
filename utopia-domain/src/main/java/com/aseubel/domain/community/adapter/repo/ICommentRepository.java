package com.aseubel.domain.community.adapter.repo;

import com.aliyuncs.exceptions.ClientException;
import com.aseubel.domain.community.model.bo.CommunityBO;
import com.aseubel.domain.community.model.entity.CommentEntity;
import com.aseubel.domain.community.model.entity.CommunityImage;

import java.time.LocalDateTime;
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
     * 获取帖子的评论列表
     * @param communityBO
     * @return
     */
    List<CommentEntity> listPostComment(CommunityBO communityBO);

    /**
     * 获取评论的图片列表
     * @param commentId
     * @return
     */
    List<String> listCommentImages(String commentId);

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

    /**
     * 点赞评论
     * @return
     */
    boolean likeComment(CommunityBO communityBO);

    /**
     * 增加评论点赞数量
     * @param commentId
     */
    void increaseLikeCount(String commentId);

    /**
     * 减少评论点赞数量
     * @param commentId
     */
    void decreaseLikeCount(String commentId);

    /**
     * 获取评论的子评论列表
     * @param communityBO
     * @return
     */
    List<CommentEntity> listSubComment(CommunityBO communityBO);

    /**
     * 删除不存在的评论图片
     * @throws ClientException
     */
    void deleteMissingImage() throws ClientException;

    /**
     * 增加评论回复数量
     * @param commentId
     */
    void increaseCommentCount(String commentId);

    /**
     * 根据评论id获取用户id
     * @param commentId
     * @return
     */
    String getUserIdByCommentId(String commentId);

    /**
     * 删除评论
     * @param communityBO
     */
    void deleteComment(CommunityBO communityBO);

    /**
     * 删除子评论
     * @param commentId
     */
    void deleteSubComment(String commentId);

    /**
     * 减少评论回复数量
     * @param commentId
     */
    void decreaseRootCommentReplyCount(String commentId);
}
