package com.aseubel.trigger.http;

import com.aliyun.credentials.Client;
import com.aliyuncs.exceptions.ClientException;
import com.aseubel.api.dto.file.UploadFileRequestDTO;
import com.aseubel.api.dto.file.UploadFileResponseDTO;
import com.aseubel.domain.sfile.model.SFileEntity;
import com.aseubel.domain.sfile.service.IFileService;
import com.aseubel.types.Response;
import com.aseubel.types.exception.AppException;
import com.aseubel.types.util.AliOSSUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

import static com.aseubel.types.enums.GlobalServiceStatusCode.OSS_DELETE_ERROR;
import static com.aseubel.types.enums.GlobalServiceStatusCode.OSS_UPLOAD_ERROR;

@Slf4j
@RestController()
@Validated
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/v1/file/") //${app.config.api-version}
@RequiredArgsConstructor
public class FileController {

    private final IFileService fileService;

    @Autowired
    private AliOSSUtil aliOSSUtil;

    /**
     * 上传文件
     */
    @PostMapping("/upload")
    public Response<UploadFileResponseDTO> upload(@Valid @RequestBody UploadFileRequestDTO uploadFileRequestDTO) {
        try {
            MultipartFile file = uploadFileRequestDTO.getFile();

            String fileURL = fileService.upload(
                    new SFileEntity(file, uploadFileRequestDTO.getFileName(), uploadFileRequestDTO.getUserId()));

            return Response.SYSTEM_SUCCESS(UploadFileResponseDTO.builder()
                    .fileURL(fileURL)
                    .build());
        } catch (NullPointerException e) {
            throw new AppException("文件名不能为空");
        } catch (ClientException e) {
            log.error("{}, code:{}, message:{}",OSS_UPLOAD_ERROR.getMessage(), e.getErrCode(), e.getErrMsg());
            throw new AppException(OSS_UPLOAD_ERROR, e);
        } catch (Exception e) {
            throw new AppException(OSS_UPLOAD_ERROR, e);
        }
    }

    /**
     * 删除文件
     */
    @DeleteMapping("/delete")
    public Response<String> delete(String filePath) {
        try {
            String fileName = aliOSSUtil.getFileName(filePath);
            aliOSSUtil.remove(fileName);
            return Response.SYSTEM_SUCCESS(fileName);
        } catch (ClientException e) {
            throw new AppException(OSS_DELETE_ERROR, e);
        }
    }

}
