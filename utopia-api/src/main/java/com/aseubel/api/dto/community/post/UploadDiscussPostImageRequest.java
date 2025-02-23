package com.aseubel.api.dto.community.post;

import com.aseubel.types.annotation.FieldDesc;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Aseubel
 * @description 上传帖子图片请求参数
 * @date 2025-01-27 11:39
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadDiscussPostImageRequest implements Serializable {

    @NotNull(message = "用户id不能为空")
    @FieldDesc(name = "用户id")
    private String userId;

    @NotNull(message = "图片文件不能为空")
    @FieldDesc(name = "图片文件")
    private MultipartFile image;

}
