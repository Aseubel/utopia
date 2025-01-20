package com.aseubel.infrastructure.convertor;

import com.aseubel.domain.sfile.model.SFileEntity;
import com.aseubel.infrastructure.dao.po.SFile;
import org.springframework.stereotype.Component;

/**
 * @author Aseubel
 * @description 分享文件实体类转换器
 * @date 2025-01-20 16:23
 */
@Component
public class SFileConvertor {

    public SFile convert(SFileEntity sFileEntity) {
        return SFile.builder()
                .createBy(sFileEntity.getUploaderId())
                .updateBy(sFileEntity.getUploaderId())
                .sfileId(sFileEntity.getSfileId())
                .sfileName(sFileEntity.getSfileName())
                .sfilePath(sFileEntity.getSfilePath())
                .sfileSize(sFileEntity.getSfileSize())
                .sfileType(sFileEntity.getSfileType())
                .downloadCount(sFileEntity.getDownloadCount())
                .build();
    }

    public SFileEntity convert(SFile sfile) {
        return SFileEntity.builder()
                .uploaderId(sfile.getCreateBy())
                .sfileId(sfile.getSfileId())
                .sfileName(sfile.getSfileName())
                .sfilePath(sfile.getSfilePath())
                .sfileSize(sfile.getSfileSize())
                .sfileType(sfile.getSfileType())
                .downloadCount(sfile.getDownloadCount())
                .build();
    }

}
