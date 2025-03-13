package com.aseubel.api.dto.community.post;

import com.aseubel.types.annotation.FieldDesc;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Aseubel
 * @date 2025-02-22 20:39
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QueryPostDetailResponse implements Serializable {

    @FieldDesc(name = "帖子id")
    private String postId;

    @FieldDesc(name = "标题")
    private String title;

    @FieldDesc(name = "内容")
    private String content;

    @FieldDesc(name = "发帖人id")
    private String userId;

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

    @FieldDesc(name = "院校代码")
    private String schoolCode;

    @FieldDesc(name = "图片列表")
    private List<String> images;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @FieldDesc(name = "发帖时间")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @FieldDesc(name = "最后更新时间")
    private LocalDateTime updateTime;

    @FieldDesc(name = "是否收藏")
    private Boolean isFavorite;

    @FieldDesc(name = "是否点赞")
    private Boolean isLike;

}
