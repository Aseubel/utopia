package com.aseubel.api.dto.community.comment;

import com.aseubel.types.annotation.FieldDesc;
import lombok.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Aseubel
 * @date 2025-02-28 12:24
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeleteCommentRequest implements Serializable {

    @FieldDesc(name = "用户id")
    @NotNull(message = "用户id不能为空")
    @NotBlank(message = "用户id不能为空")
    private String userId;

    @FieldDesc(name = "评论id")
    @NotNull(message = "评论id不能为空")
    @NotBlank(message = "评论id不能为空")
    private String commentId;

    @FieldDesc(name = "帖子id")
    private String postId;

    @FieldDesc(name = "根评论id")
    private String rootId;

    @FieldDesc(name = "学校代码")
    @NotNull(message = "学校代码不能为空")
    @NotBlank(message = "学校代码不能为空")
    private String schoolCode;

}
