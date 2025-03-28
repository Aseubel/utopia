package com.aseubel.infrastructure.dao.community;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Optional;

/**
 * @author Aseubel
 * @description 收藏记录mapper
 * @date 2025-02-07 15:52
 */
@Mapper
public interface FavoriteMapper {

    /**
     * 获取用户收藏的某个帖子id，用以判断是否有记录
     * @param userId
     * @param postId
     * @return
     */
    @Select("SELECT id FROM `favorite` WHERE user_id = #{userId} AND post_id = #{postId} AND is_deleted = 0")
    Integer getFavoritePostIdByUserIdAndPostId(String userId, String postId);

    /**
     * 判断用户是否收藏了某个帖子
     * @param userId
     * @param postId
     * @return
     */
    @Select("SELECT status FROM `favorite` WHERE user_id = #{userId} AND post_id = #{postId} AND is_deleted = 0")
    Optional<Boolean> getFavoriteStatus(String userId, String postId);

    /**
     * 获取用户收藏的帖子id列表
     * @param userId
     * @return
     */
    List<String> listUserFavoritePostId(String userId, String postId, Integer limit);

    /**
     * 获取用户收藏的帖子id列表
     * @param userId
     * @param limit
     * @return
     */
    List<String> listUserFavoritePostIdAhead(String userId, Integer limit);

    /**
     * 保存收藏记录
     * @param userId
     * @param postId
     */
    @Insert("INSERT INTO `favorite` (user_id, post_id) VALUES (#{userId}, #{postId})")
    void saveFavoriteRecord(String userId, String postId);

    /**
     * 更新收藏状态
     * @param userId
     * @param postId
     * @param status
     */
    @Update("UPDATE `favorite` SET status = #{status} WHERE user_id = #{userId} AND post_id = #{postId} AND is_deleted = 0")
    void updateFavoriteStatus(String userId, String postId, Integer status);

    /**
     * 收藏帖子
     * @param userId
     * @param postId
     */
    @Update("UPDATE `favorite` SET status = 1 WHERE user_id = #{userId} AND post_id = #{postId} AND is_delete = 0")
    void favoritePost(String userId, String postId);

    /**
     * 取消收藏帖子
     * @param userId
     * @param postId
     */
    @Update("UPDATE `favorite` SET status = 0 WHERE user_id = #{userId} AND post_id = #{postId} AND is_delete = 0")
    void disFavoritePost(String userId, String postId);

    /**
     * 删除所用用户对一个帖子的收藏记录
     * @param postId
     */
    void deletePostFavorite(String postId);
}
