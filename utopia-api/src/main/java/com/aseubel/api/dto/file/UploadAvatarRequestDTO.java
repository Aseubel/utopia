package com.aseubel.api.dto.file;

import com.aseubel.types.annotation.FieldDesc;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Aseubel
 * @description 用户上传头像请求DTO
 * @date 2025-01-21 12:30
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadAvatarRequestDTO implements Serializable {

    @NotNull(message = "用户id不能为空")
    @FieldDesc(name = "用户id")
    private String userId;

    @NotNull(message = "头像文件不能为空")
    @FieldDesc(name = "头像文件")
    private MultipartFile avatar;

}
