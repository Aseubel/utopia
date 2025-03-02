package com.aseubel.infrastructure.dao;

import com.aseubel.infrastructure.dao.po.Image;
import com.aseubel.infrastructure.dao.po.TradePost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Aseubel
 * @description 交易帖子mapper
 * @date 2025-01-30 22:52
 */
@Mapper
public interface TradePostMapper {

    /**
     * 添加交易帖子
     * @param tradePost
     */
    void addTradePost(TradePost tradePost);

    /**
     * 修改交易帖子信息
     * @param tradePost
     */
    void updateTradePost(TradePost tradePost);

    /**
     * 根据帖子id获取交易帖子信息
     * @param postId
     * @return
     */
    TradePost getTradePostByPostId(String postId);

    /**
     * 根据帖子id删除交易帖子信息
     * @param postId
     */
    void deleteTradePostByPostId(String postId);

    /**
     * 获取所有帖子信息，分页查询
     * @param postId 上一页最后一条帖子的id，用于分页查询
     * @param limit 每页显示的数量
     * @return
     */
    List<TradePost> listTradePost(String postId, Integer limit, Integer type, Integer status);

    /**
     * 获取所有帖子信息，分页查询
     * @param limit 每页显示的数量
     * @return
     */
    List<TradePost> listTradePostAhead(Integer limit, Integer type, Integer status);

    /**
     * 获取用户所有帖子信息，分页查询
     * @param postId 上一页最后一条帖子的id，用于分页查询
     * @param limit 每页显示的数量
     * @return
     */
    List<String> listUserTradePostId(String postId, Integer limit, Integer type, Integer status, String userId);

    /**
     * 获取用户所有帖子信息，分页查询
     * @param limit 每页显示的数量
     * @return
     */
    List<String> listUserTradePostAheadId(Integer limit, Integer type, Integer status, String userId);

    /**
     * 关联帖子图片
     * @param postId
     * @param images
     */
    void relateTradePostImage(String postId, List<Image> images);

    /**
     * 获取帖子第一张图片的image_id
     * @param postId
     * @return
     */
    String getPostFirstImage(String postId);

    /**
     * 删除用户发布的未完成的交易帖子
     * @param userId
     * @return
     */
    void deleteUncompletedTradePost(@Param("userId") String userId);

    /**
     * 获取交易帖信息
     * @return
     */
    List<TradePost> listDiscussPostByPostIds(List<String> postIds);

    /**
     * 根据帖子id获取帖子图片id列表
     * @param postId
     * @return
     */
    List<String> listPostImageUrlsByPostId(String postId);
}
