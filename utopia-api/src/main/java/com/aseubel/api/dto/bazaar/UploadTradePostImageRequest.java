package com.aseubel.api.dto.bazaar;

import com.aseubel.types.annotation.FieldDesc;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * @author Aseubel
 * @description 上传帖子图片请求参数
 * @date 2025-01-30 23:59
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadTradePostImageRequest implements Serializable {

    @FieldDesc(name = "用户id")
    private String userId;

    @FieldDesc(name = "图片文件")
    private MultipartFile postImage;

}
