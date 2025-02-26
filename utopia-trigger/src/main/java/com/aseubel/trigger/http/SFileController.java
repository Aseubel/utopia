package com.aseubel.trigger.http;

import com.aliyun.oss.OSSException;
import com.aliyuncs.exceptions.ClientException;
import com.aseubel.api.SFileInterface;
import com.aseubel.api.dto.file.QuerySFileRequestDTO;
import com.aseubel.api.dto.file.QuerySFileResponseDTO;
import com.aseubel.api.dto.file.UploadFileRequestDTO;
import com.aseubel.api.dto.file.UploadFileResponseDTO;
import com.aseubel.domain.sfile.model.SFileEntity;
import com.aseubel.domain.sfile.service.IFileService;
import com.aseubel.types.Response;
import com.aseubel.types.exception.AppException;
import com.aseubel.types.util.AliOSSUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static com.aseubel.types.enums.GlobalServiceStatusCode.*;

/**
 * 共享文件
 */
@Slf4j
@RestController()
@Validated
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/v1/file/") //${app.config.api-version}
@RequiredArgsConstructor
public class SFileController implements SFileInterface {

    private final IFileService fileService;

    @Autowired
    private AliOSSUtil aliOSSUtil;

    /**
     * 上传文件
     */
    @PostMapping("/upload")
    @Override
    public Response<UploadFileResponseDTO> upload(@Valid @ModelAttribute UploadFileRequestDTO uploadFileRequestDTO) {
        try {
            if (StringUtils.isEmpty(uploadFileRequestDTO.getFile().getOriginalFilename()) || StringUtils.isEmpty(uploadFileRequestDTO.getUserId())) {
                throw new AppException(PARAM_NOT_COMPLETE);
            }

            MultipartFile file = uploadFileRequestDTO.getFile();

            String fileUrl = fileService.upload(
                    new SFileEntity(file, uploadFileRequestDTO.getFileName(), uploadFileRequestDTO.getUserId(), uploadFileRequestDTO.getCourseName()));

            return Response.SYSTEM_SUCCESS(UploadFileResponseDTO.builder()
                    .fileUrl(fileUrl)
                    .build());
        } catch (AppException e) {
            throw e;
        } catch (ClientException e) {
            log.error("{}, code:{}, message:{}",OSS_UPLOAD_ERROR.getMessage(), e.getErrCode(), e.getErrMsg(), e);
            throw new AppException(OSS_UPLOAD_ERROR, e);
        } catch (Exception e) {
            log.error("未知异常", e);
            throw new AppException(OSS_UPLOAD_ERROR, e);
        }
    }

    /**
     * 下载文件
     */
    @Override
    @GetMapping("/download")
    public Response<byte[]> download(String filePath) {
        try {
            if (StringUtils.isEmpty(filePath)) {
                throw new AppException(PARAM_NOT_COMPLETE.getCode(), "文件路径不能为空");
            }
            return Response.SYSTEM_SUCCESS(fileService.download(filePath));
        } catch (AppException | OSSException e) {
            throw e;
        } catch (Exception e) {
            log.error("未知异常", e);
            throw new AppException(OSS_DOWNLOAD_ERROR, e);
        }
    }

    /**
     * 删除文件
     */
    @DeleteMapping("/delete")
    @Override
    public Response<String> delete(String filePath) {
        try {
            if (filePath == null) {
                throw new AppException("文件路径不能为空");
            }
            String fileName = aliOSSUtil.getFileName(filePath);
            aliOSSUtil.remove(fileName);
            log.info("删除文件成功, 文件名: {}", fileName);
            return Response.SYSTEM_SUCCESS(fileName);
        } catch (ClientException e) {
            throw new AppException(OSS_DELETE_ERROR, e);
        }
    }

    /**
     * 查询文件列表
     */
    @Override
    @GetMapping("/list")
    public Response<List<QuerySFileResponseDTO>> querySFiles(QuerySFileRequestDTO requestDTO) {
        List<SFileEntity> sFileEntities = fileService.listSFile(requestDTO.getFileId(), requestDTO.getLimit(), requestDTO.getSortType(), requestDTO.getCourseName());
        List<QuerySFileResponseDTO> querySFileResponseDTOS = sFileEntities.stream()
               .map(sFileEntity -> QuerySFileResponseDTO.builder()
                       .fileId(sFileEntity.getSfileId())
                       .fileName(sFileEntity.getSfileName())
                       .fileSize(sFileEntity.getSfileSize())
                       .fileType(sFileEntity.getCourseName())
                       .fileUrl(sFileEntity.getSfileUrl())
                       .downloadCount(sFileEntity.getDownloadCount())
                       .createTime(sFileEntity.getCreateTime())
                       .build())
                .collect(Collectors.toList());
        return Response.SYSTEM_SUCCESS(querySFileResponseDTOS);
    }


}
