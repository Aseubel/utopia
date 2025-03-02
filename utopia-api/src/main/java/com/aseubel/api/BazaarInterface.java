package com.aseubel.api;

import com.aseubel.api.dto.bazaar.QueryIndexTradePostRequest;
import com.aseubel.api.dto.bazaar.UploadTradePostImageResponse;
import com.aseubel.api.dto.bazaar.*;
import com.aseubel.types.Response;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Aseubel
 * @description 集市请求接口
 * @date 2025-01-23 19:23
 */
public interface BazaarInterface {

    /**
     * 查询首页帖子列表
     * @param queryIndexTradePostRequest
     * @return
     */
    Response<List<QueryIndexTradePostResponse>> queryIndexTradePost(QueryIndexTradePostRequest queryIndexTradePostRequest);

    /**
     * 上传帖子图片
     * @param uploadTradePostImageRequest
     * @return
     */
    Response<UploadTradePostImageResponse> uploadTradePostImage(@ModelAttribute UploadTradePostImageRequest uploadTradePostImageRequest);

    /**
     * 发布帖子
     * @param publishTradePostRequest
     * @return
     */
    Response publishTradePost(@Valid @RequestBody PublishTradePostRequest publishTradePostRequest);

    /**
     * 查询帖子详情
     * @param queryPostDetailRequest
     * @return
     */
    Response<QueryPostDetailResponse> queryPostDetail(QueryPostDetailRequest queryPostDetailRequest);
}
