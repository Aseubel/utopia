package com.aseubel.api.dto.user;

import com.aseubel.types.annotation.FieldDesc;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO implements Serializable {

    @FieldDesc(name = "用户微信小程序openid")
    private String userId;

    @FieldDesc(name = "token")
    private String token;

}
