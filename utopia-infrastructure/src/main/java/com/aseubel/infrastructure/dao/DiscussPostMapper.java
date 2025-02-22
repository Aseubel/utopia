package com.aseubel.infrastructure.dao;

import com.aseubel.infrastructure.dao.po.DiscussPost;
import com.aseubel.infrastructure.dao.po.Image;
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
     * 根据帖子id列表获取帖子信息列表
     * @param postIds
     * @return
     */
    List<DiscussPost> listDiscussPostByPostIds(List<String> postIds);

    /**
     * 获取所有帖子信息，分页查询
     * @param postId 上一页最后一条帖子的id，用于分页查询
     * @param limit 每页显示的数量
     * @param schoolCode 院校代码
     * @return
     */
    List<DiscussPost> listDiscussPost(String postId, Integer limit, String schoolCode, String tag);

    /**
     * 获取所有帖子信息，分页查询
     * @param limit 每页显示的数量
     * @param schoolCode 院校代码
     * @return
     */
    List<DiscussPost> listDiscussPostAhead(Integer limit, String schoolCode, String tag);

    /**
     * 关联帖子图片
     * @param postId
     * @param images
     */
    void relateDiscussPostImage(String postId, List<Image> images);

    /**
     * 获取帖子第一张图片的url
     * @param postId
     * @return
     */
    String getPostFirstImage(String postId);

    /**
     * 获取帖子图片url列表
     * @param postId
     * @return
     */
    List<String> listImageUrlByPostId(String postId);

    /**
     * 置顶帖子
     * @param userId
     * @param postId
     */
    void topPost(String userId, String postId);

    /**
     * 增加帖子的收藏数
     * @param postId
     */
    void increaseFavoriteCount(String postId);

    /**
     * 减少帖子的收藏数
     * @param postId
     */
    void decreaseFavoriteCount(String postId);

    /**
     * 增加帖子的评论数
     * @param postId
     */
    void increaseCommentCount(String postId);

    /**
     * 减少帖子的评论数
     * @param postId
     */
    void decreaseCommentCount(String postId);

    /**
     * 增加帖子的点赞数
     * @param postId
     */
    void increaseLikeCount(String postId);

    /**
     * 减少帖子的点赞数
     * @param postId
     */
    void decreaseLikeCount(String postId);

}
