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
     * 构造用户accessToken的redis key
     * utopia:user:access_token:{userId}
     * @param userId 用户id
     * @return redis key
     */
    public static String userAccessTokenKey(String userId) {
        return PREFIX + SPLIT + USER + SPLIT + ACCESS_TOKEN + SPLIT + userId;
    }

    /**
     * 构造用户refreshToken的redis key
     * utopia:user:refresh_token:{userId}
     * @param userId 用户id
     * @return redis key
     */
    public static String userRefreshTokenKey(String userId) {
        return PREFIX + SPLIT + USER + SPLIT + REFRESH_TOKEN + SPLIT + userId;
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
     * 构造帖子数据的redis key
     * utopia:discuss:post:{postId}
     * @param postId 帖子id
     * @return redis key
     */
    public static String discussPostKey(String postId) {
        return PREFIX + SPLIT + DISCUSS_POST + SPLIT + postId;
    }

    /**
     * 帖子对应的学校代码的redis key
     * @return
     */
    public static String postSchoolCodeKey(String postId) {
        return PREFIX + SPLIT + "post_school_code" + SPLIT + postId;
    }

    /**
     * 帖子评论数量的redis key
     * @return
     */
    public static String discussPostCommentCountKey() {
        return PREFIX + SPLIT + DISCUSS_POST + SPLIT +"comment_count";
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
     * 构造帖子收藏分数的redis key
     * utopia:community:post:favorite_score
     * @return
     */
    public static String postFavoriteScoreKey() {
        return PREFIX + SPLIT + DISCUSS_POST + SPLIT + "favorite_score";
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

    /**
     * 构造用户对帖子分数的redis key，用于map
     * utopia:user:post_ces_score:{userId}
     * @param userId 用户id
     */
    public static String userPostCesScoreKey(String userId) {
        return PREFIX + SPLIT + USER + SPLIT + "post_ces_score" + SPLIT + userId;
    }

    /**
     * 记录对社区帖子有行为的用户的redis key，用于set
     * utopia:community:user_has_behavior
     * @return redis key
     */
    public static String userHasBehaviorInCommunityKey() {
        return PREFIX + SPLIT + COMMUNITY + SPLIT + "user_has_behavior";
    }
}
