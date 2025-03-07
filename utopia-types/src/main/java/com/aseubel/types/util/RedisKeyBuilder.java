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
     * 构造评论的点赞数量的redis key
     * utopia:community:comment:lc:{commentId}
     * @param commentId 评论id
     * @return redis key
     */
//    public static String commentLikeCountKey(String commentId) {
//        return PREFIX + SPLIT + DISCUSS_COMMENT + SPLIT + LIKE_COUNT + SPLIT + commentId;
//    }

    /**
     * 构造用户点赞状态的redis key 用于map
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

    public static String CoursesKey() {
        return PREFIX + SPLIT + "courses";
    }

    /**
     * 构造文件重复下载的redis key
     * utopia:file_download:{userId}
     * @param userId
     * @param fileId
     * @return
     */
    public static String FileRepeatDownloadKey(String userId, String fileId) {
        return PREFIX + SPLIT + "file_download" + SPLIT + userId + SPLIT + fileId;
    }

    /**
     * 构造帖子点赞分数的redis key
     * utopia:community:post:like_score
     * @return
     */
    public static String postLikeScoreKey() {
        return PREFIX + SPLIT + DISCUSS_POST + SPLIT + "like_score";
    }

    /**
     * 构造评论点赞分数的redis key
     * utopia:community:comment:like_score:{postId}
     * @return
     */
    public static String commentLikeScoreKey(String postId) {
        return PREFIX + SPLIT + DISCUSS_COMMENT + SPLIT + "like_score" + SPLIT + postId;
    }

    /**
     * 构造评论时间分数的redis key
     * utopia:community:comment:time_score:{postId}
     * @return
     */
    public static String commentTimeScoreKey(String postId) {
        return PREFIX + SPLIT + DISCUSS_COMMENT + SPLIT + "time_score" + SPLIT + postId;
    }

    /**
     * 构造子评论点赞分数的redis key
     * utopia:community:comment:like_score_sub:{commentId}
     * @return
     */
    public static String subCommentLikeScoreKey(String commentId) {
        return PREFIX + SPLIT + DISCUSS_COMMENT + SPLIT + "like_score_sub" + SPLIT + commentId;
    }

    /**
     * 构造子评论时间分数的redis key
     * utopia:community:comment:time_score_sub:{commentId}
     * @return
     */
    public static String subCommentTimeScoreKey(String commentId) {
        return PREFIX + SPLIT + DISCUSS_COMMENT + SPLIT + "time_score_sub" + SPLIT + commentId;
    }

    /**
     * 构造评论的redis key
     * utopia:community:comment:{commentId}
     * @return
     */
    public static String commentKey(String commentId) {
        return PREFIX + SPLIT + DISCUSS_COMMENT + SPLIT + commentId;
    }

    public static String fileScoreKey() {
        return PREFIX + SPLIT + "file_score";
    }

    public static String fileKey(String fileId) {
        return PREFIX + SPLIT + "file" + SPLIT + fileId;
    }
}
