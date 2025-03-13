package com.aseubel.api.dto.community.post;

import com.aseubel.types.annotation.FieldDesc;
import lombok.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Aseubel
 * @description 删除帖子请求参数
 * @date 2025-02-16 20:26
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeletePostRequest implements Serializable {

    @FieldDesc(name = "用户id")
    @NotNull(message = "用户id不能为空")
    @NotBlank(message = "用户id不能为空")
    private String userId;

    @FieldDesc(name = "帖子id")
    @NotNull(message = "帖子id不能为空")
    @NotBlank(message = "帖子id不能为空")
    private String postId;

    @FieldDesc(name = "学校代码")
    @NotNull(message = "学校代码不能为空")
    @NotBlank(message = "学校代码不能为空")
    private String schoolCode;

}
