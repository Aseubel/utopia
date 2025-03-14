package com.aseubel.domain.search.adapter.repo;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * @author Aseubel
 * @date 2025-03-14 19:22
 */
public interface ISearchFileRepository {

    /**
     * 获取讨论帖子统计信息
     * @return json字符串
     */
    String listFileStatistics(long fileId, int pageSize) throws JsonProcessingException;

}
