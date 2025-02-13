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

    /**
     * 收藏帖子（收藏/取消收藏）
     * @param favoriteDiscussPostRequest
     * @return
     */
    Response favoriteDiscussPost(@Valid @RequestBody FavoriteDiscussPostRequest favoriteDiscussPostRequest);

    /**
     * 点赞帖子（点赞/取消点赞）
     * @param likeDiscussPostRequest
     * @return
     */
    Response likeDiscussPost(@Valid @RequestBody LikeDiscussPostRequest likeDiscussPostRequest);
}
