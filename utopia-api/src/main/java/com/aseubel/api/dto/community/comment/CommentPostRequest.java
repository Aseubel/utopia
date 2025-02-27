package com.aseubel.api.dto.community.comment;

import com.aseubel.types.annotation.FieldDesc;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
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

    @FieldDesc(name = "评论图片")
    private List<String> images;

}
