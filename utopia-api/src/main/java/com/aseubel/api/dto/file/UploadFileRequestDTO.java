package com.aseubel.api.dto.file;

import com.aseubel.types.annotation.FieldDesc;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
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
public class UploadFileRequestDTO implements Serializable {

    @NotNull(message = "文件不能为空")
    @FieldDesc(name = "文件")
    private MultipartFile file;

    @NotNull(message = "用户ID不能为空")
    @FieldDesc(name = "上传者ID")
    private String userId;

    @FieldDesc(name = "用户指定的文件名")
    private String fileName;

    @FieldDesc(name = "课程名称")
    private String courseName;

}
