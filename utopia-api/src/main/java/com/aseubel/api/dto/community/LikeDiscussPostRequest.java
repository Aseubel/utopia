package com.aseubel.api.dto.community;

import com.aseubel.types.annotation.FieldDesc;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Aseubel
 * @description 点赞请求参数
 * @date 2025-02-12 23:35
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LikeDiscussPostRequest implements Serializable {

    @NotNull(message = "用户id不能为空")
    @FieldDesc(name = "用户id")
    private String userId;

    @NotNull(message = "帖子id不能为空")
    @FieldDesc(name = "帖子id")
    private String postId;

}
