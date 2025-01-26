package com.aseubel.domain.sfile.service;

import com.aliyuncs.exceptions.ClientException;
import com.aseubel.domain.sfile.model.SFileEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author Aseubel
 * @description 文件服务接口
 * @date 2025-01-20 15:31
 */
public interface IFileService {

    /**
     * 上传文件服务
     * @param sFileEntity 文件实体
     * @return 上传成功返回文件路径，失败返回null
     */
    String upload(SFileEntity sFileEntity) throws ClientException;

    /**
     * 分页查询文件
     * @param fileId
     * @param limit
     * @param sortType
     * @return
     */
    List<SFileEntity> listSFile(String fileId, Integer limit, Integer sortType);

    /**
     * 根据文件类型查询文件
     * @param fileId
     * @param typeId
     * @param limit
     * @return
     */
    List<SFileEntity> listSFileByTypeId(String fileId, Long typeId, Integer limit);

}
