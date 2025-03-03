package com.aseubel.api.dto.user;

import com.aseubel.types.annotation.FieldDesc;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Aseubel
 * @description Refresh token request DTO
 * @date 2025-01-15 10:46
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenRequestDTO implements Serializable {

    @NotNull(message = "refreshToken cannot be null")
    @NotBlank(message = "refreshToken cannot be empty")
    @FieldDesc(name = "refreshToken")
    private String refreshToken;

    @NotNull(message = "userId cannot be null")
    @NotBlank(message = "userId cannot be empty")
    @FieldDesc(name = "userId")
    private String userId;

}
