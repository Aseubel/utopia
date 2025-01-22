package com.aseubel.infrastructure.dao;

import com.aseubel.infrastructure.dao.po.DiscussPost;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

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

    /**
     * 获取所有帖子信息，分页查询
     * @param postId 上一页最后一条帖子的id，用于分页查询
     * @param limit 每页显示的数量
     * @return
     */
    List<DiscussPost> listDiscussPost(String postId, Integer limit);

}
