package com.aseubel.infrastructure.dao;

import com.aseubel.infrastructure.dao.po.Comment;
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
     * 添加评论
     * @param comment
     */
    void addComment(Comment comment);

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
     * 根据帖子id查询评论列表
     * @param postId
     * @return
     */
    List<Comment> listCommentsByPostId(String postId);

    /**
     * 根据根/顶级评论id查询子评论列表
     * @param rootId
     * @return
     */
    List<Comment> listCommentsByRootId(String rootId);

}
