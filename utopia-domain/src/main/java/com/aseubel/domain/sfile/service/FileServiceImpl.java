package com.aseubel.domain.sfile.service;

import com.aliyuncs.exceptions.ClientException;
import com.aseubel.domain.sfile.adapter.repo.IFileRepository;
import com.aseubel.domain.sfile.model.SFileEntity;
import com.aseubel.types.util.AliOSSUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

import static com.aseubel.types.common.Constant.APP;
import static com.aseubel.types.common.Constant.PER_PAGE_FILE_SIZE;

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
        log.info("开始上传文件");
        MultipartFile file = sFileEntity.getSfile();
        // 获取文件名不包含扩展名
        String ossUrl = aliOSSUtil.upload(file, sFileEntity.generateObjectName());
        sFileEntity.setSfileUrl(ossUrl);

        fileRepository.saveSFile(sFileEntity);
        log.info("文件上传并保存成功");
        return ossUrl;
    }

    @Override
    public byte[] download(String fileUrl) throws ClientException {
        // 取到object name
        log.info("开始下载文件, fileUrl: {}", fileUrl);
        return aliOSSUtil.download(fileUrl.substring(fileUrl.indexOf(APP)));
    }

    @Override
    public List<SFileEntity> listSFile(String fileId, Integer limit, Integer sortType) {
        log.info("开始获取文件列表服务");
        limit = limit == null ? PER_PAGE_FILE_SIZE : limit;
        String sortField = "id DESC";
        if (sortType != null) {
            switch (sortType) {
                case 1:
                    sortField = "download_count DESC";
                    break;
                case 2:
                    sortField = "download_count ASC";
                    break;
                default:
                    sortField = "id DESC";
                    break;
            }
        }
        return fileRepository.listSFile(fileId, limit, sortField);
    }

    @Override
    public List<SFileEntity> listSFileByTypeId(String fileId, Long typeId, Integer limit) {
        log.info("开始获取指定类型文件列表服务");
        return fileRepository.listSFileByTypeId(fileId, typeId, limit);
    }

}
