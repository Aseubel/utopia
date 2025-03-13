package com.aseubel.api.dto.bazaar;

import com.aseubel.types.annotation.FieldDesc;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * @author Aseubel
 * @date 2025-03-02 23:51
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompleteTradeRequest {

    @FieldDesc(name = "用户id")
    @NotNull(message = "用户id不能为空")
    @NotBlank(message = "用户id不能为空")
    private String userId;

    @FieldDesc(name = "帖子id")
    @NotNull(message = "帖子id不能为空")
    @NotBlank(message = "帖子id不能为空")
    private String postId;

    @FieldDesc(name = "院校代码")
    @NotNull(message = "院校代码不能为空")
    @NotBlank(message = "院校代码不能为空")
    private String schoolCode;

}
