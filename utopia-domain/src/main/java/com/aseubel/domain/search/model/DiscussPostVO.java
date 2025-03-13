package com.aseubel.domain.search.model;

import com.aseubel.types.annotation.FieldDesc;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Aseubel
 * @date 2025-03-12 11:19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiscussPostVO {

    @FieldDesc(name = "id")
    private Long id;

    @FieldDesc(name = "用户id")
    private String userId;

    @FieldDesc(name = "帖子id")
    private String postId;

    @FieldDesc(name = "标题")
    private String title;

    @FieldDesc(name = "内容")
    private String content;

    @FieldDesc(name = "图片url")
    private String image;

    @FieldDesc(name = "标签")
    private String tag;

    @FieldDesc(name = "点赞数")
    private int likeCount;

    @FieldDesc(name = "评论数")
    private int commentCount;

    @FieldDesc(name = "收藏数")
    private int favoriteCount;

    @FieldDesc(name = "学校代号")
    private String schoolCode;

    /**
     * 转换成json字符串
     */
    public String toJsonString() {
        return "[{" +
                "\"userId\":\"" + userId + '\"' +
                ", \"postId\":\"" + postId + '\"' +
                ", \"title\":\"" + title + '\"' +
                ", \"content\":\"" + content + '\"' +
                ", \"image\":\"" + image + '\"' +
                ", \"tag\":\"" + tag + '\"' +
                ", \"likeCount\":" + likeCount +
                ", \"commentCount\":" + commentCount +
                ", \"favoriteCount\":" + favoriteCount +
                "}]";
    }

}
