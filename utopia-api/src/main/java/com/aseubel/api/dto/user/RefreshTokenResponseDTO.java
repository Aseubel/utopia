package com.aseubel.api.dto.user;

import com.aseubel.types.annotation.FieldDesc;
import lombok.*;

import java.io.Serializable;

/**
 * @author Aseubel
 * @description 刷新token返回DTO
 * @date 2025-01-15 10:39
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenResponseDTO implements Serializable {

    @FieldDesc(name = "access_token")
    private String accessToken;

    @FieldDesc(name = "refresh_token")
    private String refreshToken;

}
