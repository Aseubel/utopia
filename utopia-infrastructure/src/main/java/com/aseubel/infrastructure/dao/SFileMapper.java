package com.aseubel.infrastructure.dao;

import com.aseubel.infrastructure.dao.po.SFile;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SFileMapper {

    /**
     * 添加文件
     * @param file
     */
    void addSFile(SFile file);

    /**
     * 删除重复文件
     */
    void deleteRepeatedSFile();
}
