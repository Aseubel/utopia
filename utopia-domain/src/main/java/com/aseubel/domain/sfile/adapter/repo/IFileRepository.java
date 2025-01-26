package com.aseubel.domain.sfile.adapter.repo;

import com.aliyuncs.exceptions.ClientException;
import com.aseubel.domain.sfile.model.SFileEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    /**
     * 删除重复的分享文件
     */
    void deleteRepeatedSFile();

    /**
     * 分页查询文件
     * @param fileId
     * @param limit
     * @return
     */
    List<SFileEntity> listSFile(String fileId, Integer limit, String sortField);

    /**
     * 根据文件类型查询文件
     * @param fileId
     * @param typeId
     * @param limit
     * @return
     */
    List<SFileEntity> listSFileByTypeId(String fileId, Long typeId, Integer limit);

    /**
     * 保存文件下载记录
     * @param fileId
     * @param userId
     */
    void saveSFileDownloadRecord(String fileId, String userId);

    /**
     * 删除缺失的分享文件
     */
    void deleteMissingSFile() throws ClientException;
}
