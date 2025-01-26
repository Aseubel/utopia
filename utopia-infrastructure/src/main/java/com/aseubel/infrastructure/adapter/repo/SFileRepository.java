package com.aseubel.infrastructure.adapter.repo;

import cn.hutool.core.collection.CollectionUtil;
import com.aliyuncs.exceptions.ClientException;
import com.aseubel.domain.sfile.adapter.repo.IFileRepository;
import com.aseubel.domain.sfile.model.SFileEntity;
import com.aseubel.infrastructure.convertor.SFileConvertor;
import com.aseubel.infrastructure.dao.SFileDownloadRecordMapper;
import com.aseubel.infrastructure.dao.SFileMapper;
import com.aseubel.infrastructure.dao.UserMapper;
import com.aseubel.infrastructure.dao.po.SFile;
import com.aseubel.types.util.AliOSSUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Aseubel
 * @description 分享文件仓储层实现类
 * @date 2025-01-20 16:22
 */
@Repository
public class SFileRepository implements IFileRepository {

    @Resource
    private UserMapper userMapper;

    @Resource
    private SFileMapper sFileMapper;

    @Resource
    private SFileDownloadRecordMapper sFileDownloadRecordMapper;

    @Resource
    private SFileConvertor sFileConvertor;
    @Autowired
    private AliOSSUtil aliOSSUtil;

    @Override
    public void saveSFile(SFileEntity file) {
        sFileMapper.addSFile(sFileConvertor.convert(file));
    }

    @Override
    public void deleteRepeatedSFile() {
        sFileMapper.deleteRepeatedSFile();
    }

    @Override
    public List<SFileEntity> listSFile(String fileId, Integer limit, String sortField) {
        return Optional.ofNullable(StringUtils.isEmpty(fileId)
                        ? sFileMapper.listSFileAhead(limit, sortField)
                        : sFileMapper.listSFile(fileId, limit, sortField))
                .map(p -> p.stream().map(sFileConvertor::convert).collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    @Override
    public List<SFileEntity> listSFileByTypeId(String fileId, Long typeId, Integer limit) {
        return Optional.ofNullable(StringUtils.isEmpty(fileId)
                        ? sFileMapper.listSFileByTypeIdAhead(typeId, limit)
                        : sFileMapper.listSFileByTypeId(fileId, typeId, limit))
                .map(p -> p.stream().map(sFileConvertor::convert).collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    @Override
    public void saveSFileDownloadRecord(String fileId, String userId) {
        sFileDownloadRecordMapper.saveDownloadRecord(fileId, userId);
    }

    @Override
    public void deleteMissingSFile() throws ClientException {
        List<SFile> fileRecords = sFileMapper.listAllSFile();
        Set<String> ossRecordSet = new HashSet<>(aliOSSUtil.listObjects());
        List<String> missingFileIds = new ArrayList<>();
        // 找出数据库中存在但oss没有对应对象的记录
        for (SFile fileRecord : fileRecords) {
            if (!ossRecordSet.contains(fileRecord.getSfileUrl())) {
                missingFileIds.add(fileRecord.getId());
            }
        }
        if (!CollectionUtil.isEmpty(missingFileIds)) {
            sFileMapper.deleteMissingSFile(missingFileIds);
        }
    }

}
