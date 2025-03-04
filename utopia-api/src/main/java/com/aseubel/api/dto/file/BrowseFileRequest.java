package com.aseubel.api.dto.file;

import com.aseubel.types.annotation.FieldDesc;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Aseubel
 * @date 2025-03-04 23:51
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BrowseFileRequest implements Serializable {

    @NotNull(message = "用户id不能为空")
    @NotBlank(message = "用户id不能为空")
    @FieldDesc(name = "用户id")
    private String userId;

    @NotNull(message = "文件id不能为空")
    @NotBlank(message = "文件id不能为空")
    @FieldDesc(name = "文件id")
    private String fileId;
    
}
