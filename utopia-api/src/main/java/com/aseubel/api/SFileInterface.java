package com.aseubel.api;

import com.aliyuncs.exceptions.ClientException;
import com.aseubel.api.dto.file.*;
import com.aseubel.types.Response;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;
import java.util.List;

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
    Response<UploadFileResponseDTO> upload(@Valid @ModelAttribute UploadFileRequestDTO uploadFileRequestDTO);

    /**
     * 下载文件
     * @param filePath
     * @return
     */
    void download(String filePath, HttpServletResponse response);

    /**
     * 查看图片
     * @param filePath
     * @param response
     * @throws ClientException
     */
    void picture(String filePath, HttpServletResponse response) throws ClientException;

    /**
     * 删除文件
     * @param filePath
     * @return
     */
    Response<String> delete(String filePath);

    /**
     * 获取文件列表
     * @param querySFileRequestDTO
     * @return
     */
    Response<List<QuerySFileResponseDTO>> querySFiles(QuerySFileRequestDTO querySFileRequestDTO);

    /**
     * 获取课程列表
     * @param queryCourseRequest
     * @return
     */
    Response<List<QueryCourseResponse>> queryCourses(QueryCourseRequest queryCourseRequest);

    /**
     * 浏览文件
     * @param browseFileRequest
     * @return
     */
    Response BrowseFile(@Valid @RequestBody BrowseFileRequest browseFileRequest);
}
