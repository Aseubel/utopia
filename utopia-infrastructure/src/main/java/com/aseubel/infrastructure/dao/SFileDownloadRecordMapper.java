package com.aseubel.infrastructure.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author Aseubel
 * @description 共享文件下载记录mapper
 * @date 2025-01-26 14:13
 */
@Mapper
public interface SFileDownloadRecordMapper {

    /**
     * 保存下载记录
     * @param fileId
     * @param userId
     */
    @Insert("INSERT INTO `download_record` (sfile_id, user_id) VALUES (#{fileId}, #{userId})")
    void saveDownloadRecord(@Param("fileId") String fileId, @Param("userId") String userId);

}
