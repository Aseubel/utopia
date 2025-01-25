package com.aseubel.api;

import com.aseubel.api.dto.file.QuerySFileRequestDTO;
import com.aseubel.api.dto.file.QuerySFileResponseDTO;
import com.aseubel.api.dto.file.UploadFileRequestDTO;
import com.aseubel.types.Response;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @author Aseubel
 * @description 共享文件接口
 * @date 2025-01-20 15:22
 */
public interface SFileInterface {

    /**
     * 上传文件
     * @param uploadFileRequestDTO
     * @return
     */
    Response<String> upload(@Valid @RequestBody UploadFileRequestDTO uploadFileRequestDTO);

    /**
     * 删除文件
     * @param filePath
     * @return
     */
    Response<String> delete(@Valid String filePath);

    /**
     * 获取文件列表
     * @param querySFileRequestDTO
     * @return
     */
    Response<QuerySFileResponseDTO> querySFiles(@Valid QuerySFileRequestDTO querySFileRequestDTO);

}
