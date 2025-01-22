package com.aseubel.infrastructure.dao;

import com.aseubel.infrastructure.dao.po.DiscussPost;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Aseubel
 * @description 社区帖子数据库操作接口
 * @date 2025-01-22 12:09
 */
@Mapper
public interface DiscussPostMapper {

    /**
     * 添加帖子
     * @param discussPost
     */
    void addDiscussPost(DiscussPost discussPost);

    /**
     * 根据帖子id删除帖子
     * @param postId
     */
    void deleteDiscussPostByPostId(String postId);

    /**
     * 更新帖子信息
     * @param discussPost
     */
    void updateDiscussPost(DiscussPost discussPost);

    /**
     * 根据帖子id获取帖子信息
     * @param postId
     * @return
     */
    DiscussPost getDiscussPostByPostId(String postId);

}
