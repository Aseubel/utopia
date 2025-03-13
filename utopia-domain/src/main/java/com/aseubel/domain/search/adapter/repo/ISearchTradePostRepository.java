package com.aseubel.domain.search.adapter.repo;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * @author Aseubel
 * @date 2025-03-12 12:18
 */
public interface ISearchTradePostRepository {

    /**
     * 获取交易帖子统计信息
     * @return json字符串
     */
    String listTradePostStatistics() throws JsonProcessingException;

    /**
     * 获取交易帖子统计信息
     * @return json字符串
     */
    String listTradePostStatistics(Long postId, int pageSize) throws JsonProcessingException;
}
