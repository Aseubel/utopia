package com.aseubel.trigger.http;

import com.aliyuncs.exceptions.ClientException;
import com.aseubel.api.SFileInterface;
import com.aseubel.api.dto.file.*;
import com.aseubel.domain.sfile.model.SFileEntity;
import com.aseubel.domain.sfile.service.IFileService;
import com.aseubel.types.Response;
import com.aseubel.types.exception.AppException;
import com.aseubel.types.util.AliOSSUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

import static com.aseubel.types.enums.GlobalServiceStatusCode.*;

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

            String fileURL = fileService.upload(
                    new SFileEntity(file, uploadFileRequestDTO.getFileName(), uploadFileRequestDTO.getUserId(), uploadFileRequestDTO.getFileType()));

            return Response.SYSTEM_SUCCESS(UploadFileResponseDTO.builder()
                    .fileURL(fileURL)
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
    public Response<List<QuerySFileResponseDTO>> querySFiles(@Valid QuerySFileRequestDTO querySFileRequestDTO) {
        if (StringUtils.isEmpty(querySFileRequestDTO.getUserId())) {
            throw new AppException(PARAM_NOT_COMPLETE);
        }
        List<SFileEntity> sFileEntities = fileService.listSFile(querySFileRequestDTO.getFileId(), querySFileRequestDTO.getLimit(), querySFileRequestDTO.getSortType());
        List<QuerySFileResponseDTO> querySFileResponseDTOS = sFileEntities.stream()
               .map(sFileEntity -> QuerySFileResponseDTO.builder()
                       .fileId(sFileEntity.getSfileId())
                       .fileName(sFileEntity.getSfileName())
                       .fileSize(sFileEntity.getSfileSize())
                       .fileType(sFileEntity.getSfileType())
                       .fileUrl(sFileEntity.getSfileUrl())
                       .downloadCount(sFileEntity.getDownloadCount())
                       .createTime(sFileEntity.getCreateTime())
                       .build())
                .collect(Collectors.toList());
        return Response.SYSTEM_SUCCESS(querySFileResponseDTOS);
    }


}
