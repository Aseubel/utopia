package com.aseubel.api.dto.community.comment;

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
 * @description 查询子评论返回结果，单个response相当于一个commentEntity
 * @date 2025-02-23 20:42
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuerySubCommentResponse implements Serializable {

    @FieldDesc(name = "评论id")
    private String commentId;

    @FieldDesc(name = "用户id")
    private String userId;

    @FieldDesc(name = "用户昵称")
    private String userName;

    @FieldDesc(name = "用户头像")
    private String userAvatar;

    @FieldDesc(name = "评论内容")
    private String content;

    @FieldDesc(name = "点赞数")
    private Integer likeCount;

    @FieldDesc(name = "回复数")
    private Integer replyCount;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @FieldDesc(name = "评论时间")
    private LocalDateTime commentTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @FieldDesc(name = "评论状态修改时间")
    private LocalDateTime updateTime;

    @FieldDesc(name = "是否点赞")
    private Boolean isLike;

}
