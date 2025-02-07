package com.aseubel.infrastructure.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author Aseubel
 * @description 收藏记录mapper
 * @date 2025-02-07 15:52
 */
@Mapper
public interface FavoriteMapper {

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

}
