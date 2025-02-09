package com.aseubel.domain.community.adapter.repo;

import com.aseubel.domain.community.model.entity.CommentEntity;

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

}
