package com.aseubel.infrastructure.adapter.repo;

import com.aseubel.domain.sfile.adapter.repo.IFileRepository;
import com.aseubel.domain.sfile.model.SFileEntity;
import com.aseubel.infrastructure.convertor.SFileConvertor;
import com.aseubel.infrastructure.dao.SFileMapper;
import com.aseubel.infrastructure.dao.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
    private SFileConvertor sFileConvertor;

    @Override
    public void saveSFile(SFileEntity file) {
        sFileMapper.addSFile(sFileConvertor.convert(file));
    }

    @Override
    public void deleteRepeatedSFile() {
        sFileMapper.deleteRepeatedSFile();
    }

    @Override
    public List<SFileEntity> listSFile(String fileId, Integer limit) {
        return Optional.ofNullable(StringUtils.isEmpty(fileId)
                        ? sFileMapper.listSFileAhead(limit)
                        : sFileMapper.listSFile(fileId, limit))
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

}
