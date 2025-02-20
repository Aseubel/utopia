package com.aseubel.types.util;

import com.aseubel.types.common.RedisKey;

import static com.aseubel.types.common.RedisKey.*;

/**
 * @author Aseubel
 * @description redisKey构造 {@link RedisKey}
 * @date 2025-01-15 09:09
 */
public class RedisKeyBuilder {

    /**
     * 构造用户token的redis key
     * utopia:user:token:{userId}
     * @param userId 用户id
     * @return redis key
     */
    public static String UserTokenKey(String userId) {
        return PREFIX + SPLIT + USER_TOKEN + SPLIT + userId;
    }

    /**
     * 构造用户信息的redis key
     * utopia:user:info:{userId}
     * @param userId 用户id
     * @return redis key
     */
    public static String UserInfoKey(String userId) {
        return PREFIX + SPLIT + USER_INFO + SPLIT + userId;
    }

    /**
     * 构造帖子的点赞数量的redis key
     * utopia:discuss:post:lc:{postId}
     * @param postId 帖子id
     * @return redis key
     */
    public static String discussPostLikeCountKey(String postId) {
        return PREFIX + SPLIT + DISCUSS_POST + SPLIT + LIKE_COUNT + SPLIT + postId;
    }

    /**
     * 构造用户点赞状态的redis key
     * utopia:{userId}:ls
     * @param userId
     * @return
     */
    public static String LikeStatusKey(String userId) {
        return PREFIX + SPLIT + userId + SPLIT + LIKE_STATUS;
    }

    /**
     * 构造帖子的收藏数量的redis key
     * utopia:discuss:post:fc:{postId}
     * @param postId 帖子id
     * @return redis key
     */
    public static String discussPostFavoriteCountKey(String postId) {
        return PREFIX + SPLIT + DISCUSS_POST + SPLIT + FAVORITE_COUNT + SPLIT + postId;
    }

    /**
     * 构造用户收藏状态的redis key
     * utopia:{userId}:fs
     * @param userId
     * @return
     */
    public static String FavoriteStatusKey(String userId) {
        return PREFIX + SPLIT + userId + SPLIT + FAVORITE_STATUS;
    }

}
