package com.aseubel.api.dto.community.comment;

import com.aseubel.types.annotation.FieldDesc;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Aseubel
 * @description 规定回复评论的评论不带图片
 * @date 2025-02-16 13:52
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplyCommentRequest implements Serializable {

    @NotNull(message = "用户id不能为空")
    @FieldDesc(name = "用户id")
    private String userId;

    @NotNull(message = "帖子id不能为空")
    @FieldDesc(name = "帖子id")
    private String postId;

    @NotNull(message = "回复评论id不能为空")
    @FieldDesc(name = "回复评论id")
    private String replyTo;

    @NotNull(message = "根评论id不能为空")
    @FieldDesc(name = "根评论id")
    private String rootId;

    @NotNull(message = "评论内容不能为空")
    @FieldDesc(name = "评论内容")
    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @FieldDesc(name = "评论时间")
    private LocalDateTime commentTime;

}
