package com.aseubel.api;

import com.aseubel.api.dto.community.comment.*;
import com.aseubel.api.dto.community.post.*;
import com.aseubel.types.Response;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;
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
    Response<List<QueryIndexDiscussPostResponseDTO>> queryIndexDiscussPost(QueryIndexDiscussPostRequestDTO queryIndexDiscussPostRequestDTO);

    /**
     * 上传帖子图片
     * @param uploadDiscussPostImageRequest
     * @return
     */
    Response<UploadDiscussPostImageResponse> uploadDiscussPostImage(@Valid @ModelAttribute UploadDiscussPostImageRequest uploadDiscussPostImageRequest);

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

    /**
     * 评论帖子
     * @param commentPostRequest
     * @return
     */
    Response commentDiscussPost(@Valid @RequestBody CommentPostRequest commentPostRequest);

    /**
     * 回复评论
     * @param requestDTO 回复评论请求DTO，不带图片
     * @return
     */
    Response replyComment(@Valid @RequestBody ReplyCommentRequest requestDTO);

    /**
     * 上传评论图片
     * @param requestDTO
     * @return
     */
    Response<UploadCommentImageResponse> uploadDiscussPostImage(@ModelAttribute UploadCommentImageRequest requestDTO);

    /**
     * 查询帖子详情
     * @param requestDTO
     * @return
     */
    Response<QueryPostDetailResponse> queryPostDetail(QueryPostDetailRequest requestDTO);

    /**
     * 查询帖子评论列表
     * @param requestDTO
     * @return
     */
    Response<List<QueryPostCommentResponse>> queryPostComment(QueryPostCommentRequest requestDTO);

    /**
     * 查询子评论列表
     * @param requestDTO
     * @return
     */
    Response<List<QuerySubCommentResponse>> querySubComment(QuerySubCommentRequest requestDTO);

    /**
     * 点赞评论
     * @param requestDTO
     * @return
     */
    Response likeComment(@Valid @RequestBody LikeCommentRequest requestDTO);

    /**
     * 删除帖子
     * @param deletePostRequest
     * @return
     */
    Response deletePost(@Valid @RequestBody DeletePostRequest deletePostRequest);

    /**
     * 删除评论
     * @param deleteCommentRequest
     * @return
     */
    Response deleteComment(@Valid @RequestBody DeleteCommentRequest deleteCommentRequest);
}
