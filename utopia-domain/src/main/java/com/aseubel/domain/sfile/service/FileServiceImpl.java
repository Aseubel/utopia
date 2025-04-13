package com.aseubel.domain.sfile.service;

import com.aliyuncs.exceptions.ClientException;
import com.aseubel.domain.sfile.adapter.repo.IFileRepository;
import com.aseubel.domain.sfile.model.entity.SFileEntity;
import com.aseubel.domain.sfile.model.vo.CourseVO;
import com.aseubel.types.exception.AppException;
import com.aseubel.types.util.AliOSSUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.aseubel.types.common.Constant.APP;
import static com.aseubel.types.common.Constant.PER_PAGE_FILE_SIZE;
import static com.aseubel.types.enums.GlobalServiceStatusCode.OSS_OBJECT_NOT_EXIST;

/**
 * @author Aseubel
 * @description 文件上传服务实现类
 * @date 2025-01-20 16:33
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class FileServiceImpl implements IFileService{

    private final AliOSSUtil aliOSSUtil;

    private final IFileRepository fileRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String upload(SFileEntity sFileEntity) throws ClientException {
        MultipartFile file = sFileEntity.getSfile();
        // 获取文件名不包含扩展名
        String ossUrl = aliOSSUtil.upload(file, sFileEntity.generateObjectName());
        sFileEntity.setFileUrl(ossUrl);

        fileRepository.saveSFile(sFileEntity);
        return ossUrl;
    }

    @Override
    public void delete(String fileUrl) {
        fileRepository.deleteSFileByUrl(fileUrl);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public byte[] download(String fileUrl) throws ClientException {
        // 取到object name
        SFileEntity sFileEntity = fileRepository.getSFileByUrl(fileUrl);
        if (sFileEntity == null) {
            log.error("文件不存在, fileUrl: {}", fileUrl);
            throw new AppException(OSS_OBJECT_NOT_EXIST.getCode(), "文件不存在或已被删除");
        }
        fileRepository.incrementDownloadCount("", sFileEntity.getFileId());
        return aliOSSUtil.download(fileUrl.substring(fileUrl.indexOf(APP)));
    }

    @Override
    public byte[] simpleDownload(String fileUrl) throws ClientException {
        return aliOSSUtil.download(fileUrl.substring(fileUrl.indexOf(APP)));
    }

    @Override
    public List<SFileEntity> listSFile(String fileId, Integer limit, Integer sortType, String courseName) {
        limit = limit == null ? PER_PAGE_FILE_SIZE : limit;
        return fileRepository.listSFile(fileId, limit, sortType, courseName);
    }

    @Override
    public List<SFileEntity> listSFileByTypeId(String fileId, String courseName, Integer limit) {
        return fileRepository.listSFileByTypeId(fileId, courseName, limit);
    }

    @Override
    public List<CourseVO> queryCourses() {
        return fileRepository.queryCourses();
    }

    @Override
    public void browseFile(String userId, String fileId) {
        fileRepository.incrementDownloadCount(userId, fileId);
    }

}
