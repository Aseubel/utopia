package com.aseubel.api.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

/**
 * @author Aseubel
 * @date 2025/11/22 下午7:39
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest implements Serializable {

    @NotNull(message = "手机号不能为空")
    private String phone;

    @NotNull(message = "密码不能为空")
    private String password;

    @NotNull(message = "确认密码不能为空")
    private String confirmPassword;

    @NotNull(message = "用户名不能为空")
    private String name;
}
