package com.aseubel.api.dto.community;

import com.aseubel.types.annotation.FieldDesc;
import lombok.*;

import java.io.Serializable;

/**
 * @author Aseubel
 * @description 上传评论图片返回图片信息
 * @date 2025-02-16 14:09
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadCommentImageResponse implements Serializable {

    @FieldDesc(name = "图片id")
    private String imageId;

    @FieldDesc(name = "图片url")
    private String imageUrl;

}
