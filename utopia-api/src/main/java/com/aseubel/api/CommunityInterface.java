package com.aseubel.api;

import com.aseubel.api.dto.community.*;
import com.aseubel.types.Response;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Aseubel
 * @description 社区请求接口
 * @date 2025-01-23 19:23
 */
public interface CommunityInterface {

    /**
     * 查询首页帖子列表
     * @param queryIndexDiscussPostRequestDTO
     * @return
     */
    Response<List<QueryIndexDiscussPostResponseDTO>> queryIndexDiscussPost(@Valid QueryIndexDiscussPostRequestDTO queryIndexDiscussPostRequestDTO);

    /**
     * 上传帖子图片
     * @param uploadDiscussPostImageRequest
     * @return
     */
    Response<UploadDiscussPostImageResponse> uploadDiscussPostImage(@ModelAttribute UploadDiscussPostImageRequest uploadDiscussPostImageRequest);

    /**
     * 发布帖子
     * @param publishDiscussPostRequest
     * @return
     */
    Response publishDiscussPost(@Valid @RequestBody PublishDiscussPostRequest publishDiscussPostRequest);

}
