package com.aseubel.api.dto.file;

import com.aseubel.types.annotation.FieldDesc;
import lombok.*;

import java.io.Serializable;

/**
 * @author Aeubel
 * @description 用户上传评论图片响应DTO
 * @date 2025-01-21 12:27
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadImageResponseDTO implements Serializable {

    @FieldDesc(name = "用户id")
    private String userId;

    @FieldDesc(name = "图片id")
    private String imageId;

    @FieldDesc(name = "图片url")
    private String imageUrl;

}
