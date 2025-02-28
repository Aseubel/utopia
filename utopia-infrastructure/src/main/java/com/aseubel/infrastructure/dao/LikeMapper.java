package com.aseubel.infrastructure.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author Aseubel
 * @description 点赞mapper
 * @date 2025-02-14 18:38
 */
@Mapper
public interface LikeMapper {

    /**
     * 获取用户点赞的某个帖子/评论id(点赞记录的主键id)，用以判断是否有记录
     * @param userId
     * @param toId
     * @return
     */
    @Select("SELECT id FROM `like` WHERE user_id = #{userId} AND to_id = #{toId} AND is_deleted = 0")
    Integer getLikePostIdByUserIdAndToId(String userId, String toId);

    /**
     * 判断用户是否点赞了某个帖子/评论
     * @param userId
     * @param toId
     * @return
     */
    @Select("SELECT status FROM `like` WHERE user_id = #{userId} AND to_id = #{toId} AND is_deleted = 0")
    Optional<Boolean> getLikeStatus(String userId, String toId);

    /**
     * 获取用户点赞的帖子id列表
     * @param userId
     * @return
     */
    List<String> listUserLikePostId(String userId, String toId, Integer limit);

    /**
     * 获取用户点赞的帖子id列表
     * @param userId
     * @param limit
     * @return
     */
    List<String> listUserLikePostIdAhead(String userId, Integer limit);

    /**
     * 保存点赞记录
     * @param userId
     * @param toId
     */
    @Insert("INSERT INTO `like` (user_id, to_id, update_time) VALUES (#{userId}, #{toId}, #{updateTime})")
    void saveLikeRecord(String userId, String toId, LocalDateTime updateTime);

    /**
     * 更新点赞状态
     * @param userId 用户id
     * @param toId 被点赞的帖子/评论id
     * @param updateTime 点赞时间
     * @param status 要修改为的状态
     */
    @Update("UPDATE `like` SET status = #{status}, update_time = #{updateTime} WHERE user_id = #{userId} AND to_id = #{toId} AND update_time <= #{updateTime} AND is_deleted = 0")
    void updateLikeStatus(String userId, String toId, LocalDateTime updateTime, Integer status);

    /**
     * 删除用户的点赞记录
     * @param userId
     * @param toIds
     */
    @Update("UPDATE `like` SET is_deleted = 1 WHERE user_id = #{userId} AND to_id IN (#{toIds})")
    void deleteLikeByUserIdAndToIds(String userId, List<String> toIds);
}
