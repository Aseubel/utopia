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

    @NotNull(message = "code不能为空")
    @FieldDesc(name = "微信小程序用户登录凭证")
    private String code;
    
}
