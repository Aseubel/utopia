package com.aseubel.domain.search.adapter.repo;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Map;

/**
 * @author Aseubel
 * @date 2025-03-12 11:18
 */
public interface ISearchDiscussPostRepository {

    /**
     * 获取讨论帖子统计信息
     * @return json字符串
     */
    String listDiscussPostStatistics() throws JsonProcessingException;

    /**
     * 获取讨论帖子统计信息
     * @return json字符串
     */
    String listDiscussPostStatistics(Long postId, int pageSize) throws JsonProcessingException;

    Map<String, String> listRecentPost();
}
