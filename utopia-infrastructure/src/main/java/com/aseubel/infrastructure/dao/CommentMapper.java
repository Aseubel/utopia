package com.aseubel.infrastructure.dao;

import com.aseubel.infrastructure.dao.po.Comment;
import com.aseubel.infrastructure.dao.po.Image;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Aseubel
 * @description 评论数据库操作接口
 * @date 2025-01-22 12:33
 */
@Mapper
public interface CommentMapper {

    /**
     * 添加根评论
     * @param comment
     */
    void addRootComment(Comment comment);

    /**
     * 添加子评论
     * @param comment
     */
    void addChildComment(Comment comment);

    /**
     * 根据评论id删除评论
     * @param commentId
     */
    void deleteCommentByCommentId(String commentId);

    /**
     * 更新评论信息
     * @param comment
     */
    void updateComment(Comment comment);

    /**
     * 根据评论id查询评论信息
     * @param commentId
     * @return
     */
    Comment getCommentByCommentId(String commentId);

    /**
     * 根据评论id查询用户id
     * @param commentId
     * @return
     */
    String getUserIdByCommentId(String commentId);

    /**
     * 根据评论id列表查询用户id列表
     * @param commentIds
     * @return
     */
    List<String> listUserIdsByCommentIds(List<String> commentIds);

    /**
     * 根据帖子id查询评论列表
     * @param postId
     * @return
     */
    List<Comment> listCommentsByPostId(String postId);

    /**
     * 根据帖子id查询 3 个评论
     * @param postId
     * @return
     */
    List<Comment> listTop3CommentsByPostId(String postId);

    /**
     * 根据帖子id查询评论列表
     * @return
     */
    List<Comment> listCommentByPostId(String postId, String commentId, Integer limit, Integer sortType, int likeCount);

    /**
     * 根据帖子id查询评论列表
     * @return
     */
    List<Comment> listCommentByPostIdAhead(String postId, Integer limit, Integer sortType);

    /**
     * 根据根评论id查询评论列表
     * @return
     */
    List<Comment> listSubCommentByRootId(String rootId, String commentId, Integer limit, Integer sortType, int likeCount);

    /**
     * 根据根评论id查询评论列表
     * @return
     */
    List<Comment> listSubCommentByRootIdAhead(String rootId, Integer limit, Integer sortType);

    /**
     * 根据根/顶级评论id查询子评论列表
     * @param rootId
     * @return
     */
    List<Comment> listCommentsByRootId(String rootId);

    /**
     * 根据根/顶级评论id查询 3 个子评论
     * @param rootId
     * @return
     */
    List<Comment> listTop3CommentsByRootId(String rootId);

    /**
     * 根据评论id列表查询评论列表
     * @param commentIds
     * @return
     */
    List<Comment> listCommentByCommentIds(List<String> commentIds);

    /**
     * 根据评论id列表查询评论列表
     * @param commentIds
     * @return
     */
    List<Comment> listCommentIdAndUserIdByCommentIds(List<String> commentIds);

    /**
     * 关联评论图片
     * @param commentId
     * @param images
     */
    void relateCommentImage(String commentId, List<Image> images);

    /**
     * 获取评论图片url列表
     * @param commentId
     * @return
     */
    List<String> listImageUrlByCommentId(String commentId);

    /**
     * 增加评论点赞数
     * @param commentId
     */
    void increaseLikeCount(String commentId);

    /**
     * 减少评论点赞数
     * @param commentId
     */
    void decreaseLikeCount(String commentId);

    /**
     * 增加评论数
     * @param rootId 被回复的根评论
     */
    void increaseCommentCount(String rootId);

    /**
     * 根据用户id和帖子id查询评论id列表
     * @param userId
     * @param postId
     * @return
     */
    List<String> listCommentIdsByUserIdAndPostId(String userId, String postId);

    /**
     * 根据帖子id删除评论
     * @param postId
     */
    void deleteCommentByPostId(String postId);

    /**
     * 根据根评论id删除所有子评论
     * @param commentId
     */
    void deleteCommentByRootId(String commentId);

    /**
     * 减少评论回复数
     * @param commentId
     */
    void decreaseReplyCount(String commentId);

    /**
     * 根据评论id查询评论内容
     * @param commentIds
     * @return
     */
    List<Comment> listCommentContentByCommentIds(List<String> commentIds);
}
