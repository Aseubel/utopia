package com.aseubel.api.dto.user;

import com.aseubel.types.annotation.FieldDesc;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDTO implements Serializable {

//    @NotNull(message = "code不能为空")
    @FieldDesc(name = "微信小程序用户登录凭证")
    private String code;

    @FieldDesc(name = "用户ID")
    private String userId;

    @FieldDesc(name = "登录凭证-用户名或手机号")
    private String account;

    @FieldDesc(name = "用户密码")
    private String password;
    
}
