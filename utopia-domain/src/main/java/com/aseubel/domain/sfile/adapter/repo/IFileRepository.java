package com.aseubel.domain.sfile.adapter.repo;

import com.aseubel.domain.sfile.model.SFileEntity;
import org.springframework.stereotype.Repository;

/**
 * @author Aseubel
 * @description 分享文件仓储层接口
 * @date 2025-01-20 16:21
 */
public interface IFileRepository {

    /**
     * 保存分享文件
     * @param file 分享文件实体
     */
    void saveSFile(SFileEntity file);

}
