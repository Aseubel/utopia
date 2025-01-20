package com.aseubel.infrastructure.dao.po;

import com.aseubel.types.annotation.FieldDesc;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Aseubel
 * @description 分享文件实体类
 * @date 2025-01-20 14:04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SFile {

    @FieldDesc(name = "主键")
    private String id;

    @FieldDesc(name = "文件业务id")
    private String sfileId;

    @FieldDesc(name = "文件名称")
    private String sfileName;

    @FieldDesc(name = "文件路径")
    private String sfilePath;

    @FieldDesc(name = "文件大小")
    private Long sfileSize;

    @FieldDesc(name = "文件类型")
    private String sfileType;

    @FieldDesc(name = "下载次数")
    private Integer downloadCount;

    @FieldDesc(name = "创建者")
    private String createBy;

    @FieldDesc(name = "创建时间")
    private String createTime;

    @FieldDesc(name = "更新者")
    private String updateBy;

    @FieldDesc(name = "更新时间")
    private String updateTime;

}
