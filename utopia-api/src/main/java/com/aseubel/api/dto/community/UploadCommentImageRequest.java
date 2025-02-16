package com.aseubel.api.dto.community;

import com.aseubel.types.annotation.FieldDesc;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * @author Aseubel
 * @description 上传评论图片请求参数
 * @date 2025-02-16 14:08
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadCommentImageRequest implements Serializable {

    @FieldDesc(name = "用户id")
    private String userId;

    @FieldDesc(name = "图片文件")
    private MultipartFile image;

}
