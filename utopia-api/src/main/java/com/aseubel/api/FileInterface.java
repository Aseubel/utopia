package com.aseubel.api;

import com.aseubel.api.dto.file.UploadFileRequestDTO;
import com.aseubel.types.Response;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @author Aseubel
 * @description 共享文件接口
 * @date 2025-01-20 15:22
 */
public interface FileInterface {

    Response<String> upload(@Valid @RequestBody UploadFileRequestDTO uploadFileRequestDTO);

}
