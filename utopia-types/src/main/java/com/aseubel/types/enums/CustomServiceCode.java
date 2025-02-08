package com.aseubel.types.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author Aseubel
 * @description 业务服务编号枚举
 * @date 2025-02-07 14:38
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum CustomServiceCode {

    // 用户001-100
    USER_LOGIN(101, "用户登录"),
    USER_REGISTER(102, "新用户注册"),
    USER_UPDATE_INFO(103, "用户信息更新"),

    // 共享文件101-200
    FILE_UPLOAD(201, "文件上传"),
    FILE_DOWNLOAD(202, "文件下载"),
    FILE_DELETE(203, "文件删除"),

    // 社区201-300
    COMMUNITY_POST_PUBLISH(301, "社区帖子发布"),
    COMMUNITY_POST_DELETE(302, "社区帖子删除"),
    COMMUNITY_POST_REPLY(303, "社区帖子评论"),
    COMMUNITY_POST_LIKE(304, "社区帖子点赞"),
    COMMUNITY_POST_DISLIKE(305, "社区帖子踩"),
    COMMUNITY_POST_COLLECT(306, "社区帖子收藏"),
    COMMUNITY_POST_REPORT(307, "社区帖子举报"),
    COMMUNITY_COMMENT_PUBLISH(308, "社区评论发布"),
    COMMUNITY_COMMENT_DELETE(309, "社区评论删除"),
    COMMUNITY_COMMENT_REPLY(310, "社区评论回复"),
    COMMUNITY_COMMENT_LIKE(311, "社区评论点赞"),
    COMMUNITY_COMMENT_DISLIKE(312, "社区评论踩"),
    COMMUNITY_COMMENT_COLLECT(313, "社区评论收藏"),
    COMMUNITY_COMMENT_REPORT(314, "社区评论举报"),
        ;


    private Integer code;
    private String message;
}
