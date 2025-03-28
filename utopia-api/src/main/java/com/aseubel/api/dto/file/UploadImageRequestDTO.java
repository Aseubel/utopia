package com.aseubel.api.dto.file;

import com.aseubel.types.annotation.FieldDesc;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Aseubel
 * @description 评论时上传图片DTO
 * @date 2025-01-21 12:25
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadImageRequestDTO implements Serializable {

    @NotNull(message = "用户id不能为空")
    @NotBlank(message = "用户id不能为空")
    @FieldDesc(name = "用户id")
    private String userId;

    @NotNull(message = "图片文件不能为空")
    @FieldDesc(name = "图片文件")
    private MultipartFile image;

}
