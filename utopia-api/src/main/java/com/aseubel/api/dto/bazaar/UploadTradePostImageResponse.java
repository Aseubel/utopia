package com.aseubel.api.dto.bazaar;

import com.aseubel.types.annotation.FieldDesc;
import lombok.*;

import java.io.Serializable;

/**
 * @author Aseubel
 * @description 上传帖子图片返回图片信息
 * @date 2025-01-31 00:00
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadTradePostImageResponse implements Serializable {

    @FieldDesc(name = "图片id")
    private String imageId;

    @FieldDesc(name = "图片url")
    private String imageUrl;

}
