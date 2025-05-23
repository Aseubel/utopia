package com.aseubel.trigger.http;

import com.aseubel.api.SearchInterface;
import com.aseubel.api.dto.search.ApiSearchResponse;
import com.aseubel.api.dto.search.SearchRequest;
import com.aseubel.domain.search.model.SearchBO;
import com.aseubel.domain.search.model.SearchResponse;
import com.aseubel.domain.search.service.ISearchService;
import com.aseubel.types.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 搜索
 */
@Slf4j
@RestController()
@Validated
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/v1/search/") //${app.config.api-version}
@RequiredArgsConstructor
public class SearchController implements SearchInterface {

    private final ISearchService searchService;

    /**
     * 社区搜索
     */
    @Override
    @GetMapping("community")
    public Response<ApiSearchResponse> communitySearch(SearchRequest searchRequest) {
        SearchBO searchBO = SearchBO.builder()
               .query(searchRequest.getQuery())
               .offset(searchRequest.getOffset())
               .limit(searchRequest.getLimit())
                .schoolCode(searchRequest.getSchoolCode())
               .build();
        SearchResponse searchResponse = searchService.searchDiscussPost(searchBO);
        return Response.SYSTEM_SUCCESS(convert(searchResponse));
    }

    /**
     * 集市搜索
     */
    @Override
    @GetMapping("bazaar")
    public Response<ApiSearchResponse> bazaarSearch(SearchRequest searchRequest) {
        SearchBO searchBO = SearchBO.builder()
                .query(searchRequest.getQuery())
                .offset(searchRequest.getOffset())
                .limit(searchRequest.getLimit())
                .schoolCode(searchRequest.getSchoolCode())
                .build();
        SearchResponse searchResponse = searchService.searchTradePost(searchBO);
        return Response.SYSTEM_SUCCESS(convert(searchResponse));
    }

    /**
     * 文件搜索
     */
    @Override
    @GetMapping("file")
    public Response<ApiSearchResponse> fileSearch(SearchRequest searchRequest) {
        SearchBO searchBO = SearchBO.builder()
                .query(searchRequest.getQuery())
                .offset(searchRequest.getOffset())
                .limit(searchRequest.getLimit())
                .schoolCode(searchRequest.getSchoolCode())
                .build();
        SearchResponse searchResponse = searchService.searchFile(searchBO);
        return Response.SYSTEM_SUCCESS(convert(searchResponse));
    }

    private ApiSearchResponse convert(SearchResponse searchResponse) {
        return ApiSearchResponse.builder()
                .searchResult(searchResponse.getSearchResult())
                .totalCount(searchResponse.getTotalCount())
                .searchTime(searchResponse.getSearchTime())
                .build();
    }
}
