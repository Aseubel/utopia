package com.aseubel.domain.search.service;

import com.aseubel.domain.search.model.SearchBO;
import com.aseubel.domain.search.model.SearchResponse;
import com.aseubel.types.common.Constant;
import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.SearchRequest;
import com.meilisearch.sdk.model.SearchResult;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.aseubel.types.common.Constant.*;

/**
 * @author Aseubel
 * @date 2025-03-12 10:14
 */
@Service
@Slf4j
public class SearchService implements ISearchService{

    @Resource
    private Client meilisearchClient;

    @Override
    public SearchResponse searchDiscussPost(SearchBO searchBO) {
        SearchRequest searchRequest =
                SearchRequest.builder()
                        .q(searchBO.getQuery())
                        .attributesToSearchOn(SEARCH_FIELD)
                        .attributesToHighlight(SEARCH_FIELD)
                        .sort(SEARCH_SORT)
                        .offset(searchBO.getOffset())
                        .limit(searchBO.getLimit())
                        .build();
        SearchResult result = (SearchResult) meilisearchClient.index(Constant.getDiscussPostSearchIndex(searchBO.getSchoolCode())).search(searchRequest);
        return SearchResponse.builder()
                        .searchResult(result.getHits())
                        .totalCount(result.getEstimatedTotalHits())
                        .searchTime(result.getProcessingTimeMs())
                        .build();
    }

    @Override
    public SearchResponse searchTradePost(SearchBO searchBO) {
        SearchRequest searchRequest =
                SearchRequest.builder()
                        .q(searchBO.getQuery())
                        .attributesToHighlight(SEARCH_FIELD)
                        .offset(searchBO.getOffset())
                        .limit(searchBO.getLimit())
                        .build();
        SearchResult result = (SearchResult) meilisearchClient.index(Constant.getTradePostSearchIndex(searchBO.getSchoolCode())).search(searchRequest);
        return SearchResponse.builder()
                .searchResult(result.getHits())
                .totalCount(result.getEstimatedTotalHits())
                .searchTime(result.getProcessingTimeMs())
                .build();
    }
}
