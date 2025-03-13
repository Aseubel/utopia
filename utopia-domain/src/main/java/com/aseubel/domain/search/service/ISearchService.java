package com.aseubel.domain.search.service;

import com.aseubel.domain.search.model.SearchBO;
import com.aseubel.domain.search.model.SearchResponse;

import java.util.List;

/**
 * @author Aseubel
 * @date 2025-03-12 10:06
 */
public interface ISearchService {

    /**
     * 搜索讨论帖
     * @return 搜索结果
     */
    SearchResponse searchDiscussPost(SearchBO searchBO);

    /**
     * 搜索讨论帖
     * @return 搜索结果
     */
    SearchResponse searchTradePost(SearchBO searchBO);

}
