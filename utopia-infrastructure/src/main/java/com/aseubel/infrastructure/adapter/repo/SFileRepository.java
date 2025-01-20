package com.aseubel.infrastructure.adapter.repo;

import com.aseubel.domain.sfile.adapter.repo.IFileRepository;
import com.aseubel.domain.sfile.model.SFileEntity;
import com.aseubel.infrastructure.convertor.SFileConvertor;
import com.aseubel.infrastructure.dao.SFileMapper;
import com.aseubel.infrastructure.dao.UserMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

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

}
