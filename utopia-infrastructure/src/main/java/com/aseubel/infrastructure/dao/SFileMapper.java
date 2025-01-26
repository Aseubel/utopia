package com.aseubel.infrastructure.dao;

import com.aseubel.infrastructure.dao.po.SFile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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

    /**
     * 分页查询文件
     * @param fileId
     * @param limit
     * @param sortField
     * @return
     */
    List<SFile> listSFile(@Param("fileId") String fileId, @Param("limit") Integer limit, @Param("sortField") String sortField);

    /**
     * 分页查询文件
     * @param limit
     * @param sortField
     * @return
     */
    List<SFile> listSFileAhead(@Param("limit") Integer limit, @Param("sortField") String sortField);

    /**
     * 根据文件类型查询文件
     * @param fileId
     * @param typeId
     * @param limit
     * @return
     */
    List<SFile> listSFileByTypeId(String fileId, Long typeId, Integer limit);

    /**
     * 根据文件类型查询文件
     * @param typeId
     * @param limit
     * @return
     */
    List<SFile> listSFileByTypeIdAhead(Long typeId, Integer limit);

}
