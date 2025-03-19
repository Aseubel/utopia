package com.aseubel.api;

import com.aseubel.api.dto.search.ApiSearchResponse;
import com.aseubel.api.dto.search.SearchRequest;
import com.aseubel.types.Response;

/**
 * @author Aseubel
 * @description 搜索接口
 * @date 2025-03-11 22:18
 */
public interface SearchInterface {

    /**
     * 社区搜索讨论帖
     * @return
     */
    Response<ApiSearchResponse> communitySearch(SearchRequest searchRequest);

    /**
     * 集市搜索交易帖
     * @return
     */
    Response<ApiSearchResponse> bazaarSearch(SearchRequest searchRequest);

    /**
     * 文件搜索
     * @param searchRequest
     * @return
     */
    Response<ApiSearchResponse> fileSearch(SearchRequest searchRequest);

}
