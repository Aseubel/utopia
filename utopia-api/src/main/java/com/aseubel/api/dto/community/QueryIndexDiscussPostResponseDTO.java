package com.aseubel.api.dto.community;

import com.aseubel.types.annotation.FieldDesc;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Aseubel
 * @description 查询首页帖子列表返回DTO
 * @date 2025-01-23 19:18
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QueryIndexDiscussPostResponseDTO implements Serializable {

    @FieldDesc(name = "帖子id")
    private String discussPostId;

    @FieldDesc(name = "用户id")
    private String userId;

    @FieldDesc(name = "用户昵称")
    private String userName;

    @FieldDesc(name = "用户头像")
    private String userAvatar;

    @FieldDesc(name = "标题")
    private String title;

    @FieldDesc(name = "内容")
    private String content;

    @FieldDesc(name = "标签")
    private String tag;

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

    @FieldDesc(name = "主要评论列表")
    private Object comments;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @FieldDesc(name = "创建时间")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @FieldDesc(name = "更新时间")
    private LocalDateTime updateTime;

    @FieldDesc(name = "图片url，因为展示在首页，所以只展示第一张图片")
    private String image;

    @FieldDesc(name = "是否收藏")
    private Boolean isFavorite;

    @FieldDesc(name = "是否点赞")
    private Boolean isLike;

}
