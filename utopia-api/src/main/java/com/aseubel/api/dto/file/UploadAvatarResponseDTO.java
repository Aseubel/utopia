package com.aseubel.api.dto.file;

import com.aseubel.types.annotation.FieldDesc;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Aeubel
 * @description 用户上传头像响应DTO
 * @date 2025-01-21 12:27
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadAvatarResponseDTO implements Serializable {

    @FieldDesc(name = "用户id")
    private String userId;

    @FieldDesc(name = "头像id")
    private String avatarId;

    @FieldDesc(name = "头像url")
    private String avatarUrl;

}
