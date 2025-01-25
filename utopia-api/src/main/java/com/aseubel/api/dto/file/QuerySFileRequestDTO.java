package com.aseubel.api.dto.file;

import com.aseubel.types.annotation.FieldDesc;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * @author Aseubel
 * @description 查询分享文件列表请求DTO
 * @date 2025-01-25 14:37
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuerySFileRequestDTO {

    @NotNull(message = "用户id不能为空")
    @FieldDesc(name = "用户id")
    private String userId;

    @FieldDesc(name = "上一页最后的文件id")
    private String fileId;

    @FieldDesc(name = "页大小")
    private Integer limit;

}
