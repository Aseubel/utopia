package com.aseubel.domain.community.model.entity;

import com.aseubel.types.annotation.FieldDesc;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author Aseubel
 * @description 社区讨论帖子实体类
 * @date 2025-01-22 12:21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiscussPostEntity {

    @FieldDesc(name = "id")
    private Long id;

    @FieldDesc(name = "帖子id")
    private String postId;

    @FieldDesc(name = "用户id")
    private String userId;

    @FieldDesc(name = "所属院校代码")
    private String schoolCode;

    @FieldDesc(name = "用户昵称")
    private String userName;

    @FieldDesc(name = "用户头像")
    private String userAvatar;

    @FieldDesc(name = "标题")
    private String title;

    @FieldDesc(name = "内容")
    private String content;

    @FieldDesc(name = "点赞数")
    private Integer likeCount;

    @FieldDesc(name = "评论数")
    private Integer commentCount;

    @FieldDesc(name = "收藏数")
    private Integer favoriteCount;

    @FieldDesc(name = "0-普通;1-置顶")
    private Integer type;

    @FieldDesc(name = "0-普通;1-封禁")
    private Integer status;

    @FieldDesc(name = "标签")
    private String tag;

    @FieldDesc(name = "创建时间")
    private LocalDateTime createTime;

    @FieldDesc(name = "更新时间")
    private LocalDateTime updateTime;

    @FieldDesc(name = "图片list")
    private List<String> images;

    @FieldDesc(name = "第一张图片")
    private String image;

    @FieldDesc(name = "评论list")
    private List<CommentEntity> comments;

    @FieldDesc(name = "是否收藏")
    private Boolean isFavorite;

    @FieldDesc(name = "是否点赞")
    private Boolean isLike;

    public void generatePostId() {
        this.postId = "dp_" + UUID.randomUUID().toString().replaceAll("-", "");
    }

}
