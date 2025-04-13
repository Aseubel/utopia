package com.aseubel.trigger.http;

import com.aliyun.oss.OSSException;
import com.aliyuncs.exceptions.ClientException;
import com.aseubel.api.SFileInterface;
import com.aseubel.api.dto.file.*;
import com.aseubel.domain.sfile.model.entity.SFileEntity;
import com.aseubel.domain.sfile.model.vo.CourseVO;
import com.aseubel.domain.sfile.service.IFileService;
import com.aseubel.types.Response;
import com.aseubel.types.exception.AppException;
import com.aseubel.types.util.AliOSSUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import static com.aseubel.types.common.Constant.APP;
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
    public void download(String filePath, HttpServletResponse response) {
        try (OutputStream os = response.getOutputStream()){
            if (StringUtils.isEmpty(filePath)) {
                throw new AppException(PARAM_NOT_COMPLETE.getCode(), "文件路径不能为空");
            }
            byte[] fileBytes = fileService.download(filePath);

            // 获取文件的 MIME 类型
            String contentType = getContentType(filePath);

            // 设置响应头
            response.setContentType(contentType);
            response.setContentLength(fileBytes.length);
            response.setHeader("Content-Disposition", "attachment; filename=" + encodeFileName(filePath.substring(filePath.lastIndexOf('/')+1)));

            os.write(fileBytes);
            os.flush();
        } catch (AppException | OSSException e) {
            throw e;
        } catch (Exception e) {
            log.error("未知异常", e);
            throw new AppException(OSS_DOWNLOAD_ERROR, e);
        }
    }

    private String getContentType(String filename) {
        String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
        return switch (extension) {
            case "doc" -> MediaType.APPLICATION_OCTET_STREAM_VALUE; // 或 "application/msword"
            case "docx" -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case "ppt" -> "application/vnd.ms-powerpoint";
            case "pptx" -> "application/vnd.openxmlformats-officedocument.presentationml.presentation";
            case "pdf" -> MediaType.APPLICATION_PDF_VALUE;
            default -> MediaType.APPLICATION_OCTET_STREAM_VALUE;
        };
    }

    // 编码文件名（处理中文文件名）
    private String encodeFileName(String filename) {
        try {
            return URLEncoder.encode(filename, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return filename;
        }
    }

    @Override
    @GetMapping("/picture")
    public void picture(String filePath, HttpServletResponse response) throws ClientException {
        byte[] fileBytes = fileService.simpleDownload(filePath);
        // 设置响应头
        response.setContentType("image/jpeg");
        response.setContentLength(fileBytes.length);
        // 写入图片数据
        try (OutputStream os = response.getOutputStream()) {
            os.write(fileBytes);
            os.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
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
            fileService.delete(filePath);
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
                       .fileId(sFileEntity.getFileId())
                       .fileName(sFileEntity.getFileName())
                       .fileSize(sFileEntity.getFileSize())
                       .fileType(sFileEntity.getCourseName())
                       .fileUrl(sFileEntity.getFileUrl())
                       .downloadCount(sFileEntity.getDownloadCount())
                       .createTime(sFileEntity.getCreateTime())
                       .build())
                .collect(Collectors.toList());
        return Response.SYSTEM_SUCCESS(querySFileResponseDTOS);
    }

    /**
     * 查询课程列表
     */
    @Override
    @GetMapping("/course")
    public Response<List<QueryCourseResponse>> queryCourses(QueryCourseRequest queryCourseRequest) {
        List<CourseVO> courseVOS = fileService.queryCourses();
        List<QueryCourseResponse> queryCourseResponses = courseVOS.stream()
               .map(courseVO -> QueryCourseResponse.builder()
                       .majorName(courseVO.getMajorName())
                       .courseNames(courseVO.getCourseName())
                       .build())
               .collect(Collectors.toList());
        return Response.SYSTEM_SUCCESS(queryCourseResponses);
    }

    /**
     * 浏览文件，增加下载次数
     * @param browseFileRequest
     * @return
     */
    @Override
    @PutMapping("/browse")
    public Response BrowseFile(@Valid @RequestBody BrowseFileRequest browseFileRequest) {
        fileService.browseFile(browseFileRequest.getUserId(), browseFileRequest.getFileId());
        return Response.SYSTEM_SUCCESS();
    }

}
