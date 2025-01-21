package com.aseubel.domain.sfile.service;

import com.aliyuncs.exceptions.ClientException;
import com.aseubel.domain.sfile.adapter.repo.IFileRepository;
import com.aseubel.domain.sfile.model.SFileEntity;
import com.aseubel.types.util.AliOSSUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

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
        String ossUrl = aliOSSUtil.upload(file, sFileEntity.getObjectName());
        sFileEntity.setSfileUrl(ossUrl);

        fileRepository.saveSFile(sFileEntity);
        log.info("文件上传并保存成功");
        return ossUrl;
    }

}
