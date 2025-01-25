package com.aseubel.api.dto.file;

import com.aseubel.types.annotation.FieldDesc;
import lombok.*;

/**
 * @author Aseubel
 * @description 查询分享文件列表响应DTO
 * @date 2025-01-25 14:37
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuerySFileResponseDTO {

    @FieldDesc(name = "文件id")
    private String fileId;

    @FieldDesc(name = "文件名称")
    private String fileName;

    @FieldDesc(name = "文件大小")
    private Long fileSize;

    @FieldDesc(name = "文件url")
    private String fileUrl;

    @FieldDesc(name = "创建时间")
    private String createTime;

    @FieldDesc(name = "下载数量")
    private String downloadCount;

    @FieldDesc(name = "文件所属分类")
    private String fileType;

}
