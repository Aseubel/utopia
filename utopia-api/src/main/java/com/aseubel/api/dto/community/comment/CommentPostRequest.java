package com.aseubel.api.dto.community.comment;

import com.aseubel.types.annotation.FieldDesc;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Aseubel
 * @date 2025-02-16 13:52
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentPostRequest implements Serializable {

    @NotNull(message = "用户id不能为空")
    @NotBlank(message = "用户id不能为空")
    @FieldDesc(name = "用户id")
    private String userId;

    @NotNull(message = "帖子id不能为空")
    @NotBlank(message = "帖子id不能为空")
    @FieldDesc(name = "帖子id")
    private String postId;

    @NotNull(message = "评论内容不能为空")
    @NotBlank(message = "评论内容不能为空")
    @FieldDesc(name = "评论内容")
    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @FieldDesc(name = "评论时间")
    private LocalDateTime commentTime;

    @FieldDesc(name = "评论图片")
    private List<String> images;

}
