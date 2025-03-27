package com.aseubel.domain.sfile.adapter.repo;

import com.aliyuncs.exceptions.ClientException;
import com.aseubel.domain.sfile.model.entity.SFileEntity;
import com.aseubel.domain.sfile.model.vo.CourseVO;

import java.util.List;

/**
 * @author Aseubel
 * @description 分享文件仓储层接口
 * @date 2025-01-20 16:21
 */
public interface IFileRepository {

    /**
     * 保存分享文件
     * @param file 分享文件实体
     */
    void saveSFile(SFileEntity file);

    /**
     * 删除重复的分享文件
     */
    void deleteRepeatedSFile();

    /**
     * 分页查询文件
     * @param fileId
     * @param limit
     * @return
     */
    List<SFileEntity> listSFile(String fileId, Integer limit, Integer sortType, String courseName);

    /**
     * 根据文件类型查询文件
     * @param fileId
     * @param courseName
     * @param limit
     * @return
     */
    List<SFileEntity> listSFileByTypeId(String fileId, String courseName, Integer limit);

    /**
     * 保存文件下载记录
     * @param fileId
     * @param userId
     */
    void saveSFileDownloadRecord(String fileId, String userId);

    /**
     * 删除缺失的分享文件
     */
    void deleteMissingSFile() throws ClientException;

    /**
     * 根据文件id查询文件
     * @param fileId
     * @return
     */
    SFileEntity getSFileById(String fileId);

    /**
     * 根据文件url查询文件
     * @param fileUrl
     * @return
     */
    SFileEntity getSFileByUrl(String fileUrl);

    /**
     * 增加下载次数
     * @param userId
     * @param fileId
     */
    void incrementDownloadCount(String userId, String fileId);

    /**
     * 查询所有课程信息
     * @return
     */
    List<CourseVO> queryCourses();

    /**
     * 根据文件url删除文件
     * @param fileUrl
     */
    void deleteSFileByUrl(String fileUrl);
}
