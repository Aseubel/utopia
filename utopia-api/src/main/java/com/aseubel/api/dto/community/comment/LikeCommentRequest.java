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
 * @date 2025-02-23 20:17
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LikeCommentRequest implements Serializable {

    @NotNull(message = "用户id不能为空")
    @FieldDesc(name = "用户id")
    private String userId;

    @NotNull(message = "评论id不能为空")
    @FieldDesc(name = "评论id")
    private String commentId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @NotNull(message = "点赞时间不能为空")
    @FieldDesc(name = "点赞时间")
    private LocalDateTime likeTime;

    @FieldDesc(name = "帖子id")
    private String postId;

    @FieldDesc(name = "根评论id")
    private String rootId;
}
