package com.aseubel.api.dto.message;

import com.aseubel.types.annotation.FieldDesc;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Aseubel
 * @date 2025-03-19 23:23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReadMessageRequest implements Serializable {

    @FieldDesc(name = "用户id")
    @NotNull(message = "用户id不能为空")
    @NotBlank(message = "用户id不能为空")
    private String userId;

    @FieldDesc(name = "另一人id")
    @NotNull(message = "另一人id不能为空")
    @NotBlank(message = "另一人id不能为空")
    private String otherId;

    @FieldDesc(name = "消息id")
    @NotNull(message = "消息id不能为空")
    @NotBlank(message = "消息id不能为空")
    private String messageId;

}
