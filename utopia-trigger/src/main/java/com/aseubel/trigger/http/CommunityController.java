package com.aseubel.trigger.http;

import com.aliyuncs.exceptions.ClientException;
import com.aseubel.api.CommunityInterface;
import com.aseubel.api.dto.community.comment.*;
import com.aseubel.api.dto.community.post.*;
import com.aseubel.domain.community.model.bo.CommunityBO;
import com.aseubel.domain.community.model.entity.CommentEntity;
import com.aseubel.domain.community.model.entity.CommunityImage;
import com.aseubel.domain.community.model.entity.DiscussPostEntity;
import com.aseubel.domain.community.service.ICommunityService;
import com.aseubel.types.Response;
import com.aseubel.types.event.CommentPostEvent;
import com.aseubel.types.event.CustomEvent;
import com.aseubel.types.event.ReplyCommentEvent;
import com.aseubel.types.exception.AppException;
import com.aseubel.types.util.CustomMultipartFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.aseubel.types.enums.CustomServiceCode.COMMUNITY_POST_PUBLISH;
import static com.aseubel.types.enums.GlobalServiceStatusCode.OSS_UPLOAD_ERROR;
import static com.aseubel.types.enums.GlobalServiceStatusCode.PARAM_NOT_COMPLETE;

/**
 * 社区
 */
@Slf4j
@RestController()
@Validated
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/v1/community/") //${app.config.api-version}
@RequiredArgsConstructor
public class CommunityController implements CommunityInterface {

    private final ICommunityService communityService;

    private final ApplicationEventPublisher eventPublisher;

    /**
     * 查询首页帖子列表
     */
    @Override
    @GetMapping("/post")
    public Response<List<QueryIndexDiscussPostResponseDTO>> queryIndexDiscussPost(@Valid QueryIndexDiscussPostRequestDTO requestDTO) {
        CommunityBO communityBO = CommunityBO.builder()
                .userId(requestDTO.getUserId())
                .postId(requestDTO.getPostId())
                .limit(requestDTO.getLimit())
                .schoolCode(requestDTO.getSchoolCode())
                .tag(requestDTO.getTag())
                .build();

        List<DiscussPostEntity> discussPosts = communityService.listDiscussPost(communityBO);

        List<QueryIndexDiscussPostResponseDTO> responseDTOs = new ArrayList<>();
        for (DiscussPostEntity discussPost : discussPosts) {
            responseDTOs.add(QueryIndexDiscussPostResponseDTO.builder()
                    .discussPostId(discussPost.getDiscussPostId())
                    .userId(discussPost.getUserId())
                    .userName(discussPost.getUserName())
                    .userAvatar(discussPost.getUserAvatar())
                    .title(discussPost.getTitle())
                    .content(discussPost.getContent())
                    .tag(discussPost.getTag())
                    .likeCount(discussPost.getLikeCount())
                    .commentCount(discussPost.getCommentCount())
                    .favoriteCount(discussPost.getFavoriteCount())
                    .type(discussPost.getType())
                    .status(discussPost.getStatus())
                    .comments(discussPost.getComments())
                    .createTime(discussPost.getCreateTime())
                    .updateTime(discussPost.getUpdateTime())
                    .image(discussPost.getImage())
                    .isFavorite(discussPost.getIsFavorite())
                    .isLike(discussPost.getIsLike())
                    .build());
        }
        return Response.SYSTEM_SUCCESS(responseDTOs);
    }

    /**
     * 上传帖子图片
     */
    @Override
    @PostMapping("/post/image")
    public Response<UploadDiscussPostImageResponse> uploadDiscussPostImage(@Valid @ModelAttribute UploadDiscussPostImageRequest requestDTO) {
        if (imageOrUserIdIsBlank(requestDTO)) {
            throw new AppException(PARAM_NOT_COMPLETE);
        }
        MultipartFile file = requestDTO.getImage();
        // 使用Thumbnailator进行压缩
        try (InputStream inputStream = file.getInputStream();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Thumbnails.of(inputStream)
                    .useExifOrientation(true) // 避免额外旋转计算
                    .size(1024, 1024) // 设置压缩尺寸
                    .keepAspectRatio(true) // 保持纵横比
                    .outputQuality(0.5) // 设置输出质量（0.0到1.0之间）
                    .outputFormat("JPEG") // 设置输出格式
                    .toOutputStream(outputStream);
            // 将压缩后的图片转换为Base64字符串
            byte[] compressedBytes = outputStream.toByteArray();
            CommunityImage resultImage = communityService.uploadPostImage(
                    CommunityImage.builder()
                            .userId(requestDTO.getUserId())
                            .image(new CustomMultipartFile(compressedBytes, file.getOriginalFilename()))
                            .build());

            return Response.SYSTEM_SUCCESS(
                    UploadDiscussPostImageResponse.builder()
                            .imageId(resultImage.getImageId())
                            .imageUrl(resultImage.getImageUrl())
                            .build());
        } catch (ClientException e) {
            log.error("上传帖子图片时oss客户端异常，{}, code:{}, message:{}", OSS_UPLOAD_ERROR.getMessage(), e.getErrCode(), e.getErrMsg(), e);
            throw new AppException(OSS_UPLOAD_ERROR, e);
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            log.error("上传帖子图片时出现未知异常", e);
            throw new AppException(OSS_UPLOAD_ERROR, e);
        }
    }

    /**
     * 发布帖子
     */
    @Override
    @PostMapping("/post")
    public Response<PublishDiscussPostResponse> publishDiscussPost(@Valid @RequestBody PublishDiscussPostRequest publishDiscussPostRequest) {
        DiscussPostEntity discussPostEntity = DiscussPostEntity.builder()
                .userId(publishDiscussPostRequest.getUserId())
                .schoolCode(publishDiscussPostRequest.getSchoolCode())
                .title(publishDiscussPostRequest.getTitle())
                .content(publishDiscussPostRequest.getContent())
                .tag(publishDiscussPostRequest.getTag())
                .images(publishDiscussPostRequest.getImages())
                .build();
        communityService.publishDiscussPost(discussPostEntity);

        eventPublisher.publishEvent(new CustomEvent(discussPostEntity, COMMUNITY_POST_PUBLISH));
        return Response.SYSTEM_SUCCESS(PublishDiscussPostResponse.builder()
                        .userId(discussPostEntity.getUserId())
                        .postId(discussPostEntity.getDiscussPostId())
                        .title(discussPostEntity.getTitle())
                        .content(discussPostEntity.getContent())
                        .tag(discussPostEntity.getTag())
                        .image(discussPostEntity.getImage())
                        .build());
    }

    /**
     * 收藏帖子（收藏/取消收藏）
     */
    @Override
    @PutMapping("/post/favorite")
    public Response favoriteDiscussPost(@Valid @RequestBody FavoriteDiscussPostRequest requestDTO) {
        communityService.favoriteDiscussPost(requestDTO.getUserId(), requestDTO.getPostId());
//        eventPublisher.publishEvent(new FavoriteEvent(CommunityBO.builder()
//                .userId(requestDTO.getUserId())
//                .postId(requestDTO.getPostId())
//                .build()));
        return Response.SYSTEM_SUCCESS();
    }

    /**
     * 点赞帖子（点赞/取消点赞）
     */
    @Override
    @PutMapping("/post/like")
    public Response likeDiscussPost(@Valid @RequestBody LikeDiscussPostRequest requestDTO) {
        CommunityBO communityBO = CommunityBO.builder()
                .userId(requestDTO.getUserId())
                .postId(requestDTO.getPostId())
                .eventTime(requestDTO.getLikeTime())
                .build();
        communityService.likeDiscussPost(communityBO);
//        eventPublisher.publishEvent(new LikeEvent(CommunityBO.builder()
//                .userId(requestDTO.getUserId())
//                .postId(requestDTO.getPostId())
//                .eventTime(requestDTO.getLikeTime())
//                .build()));
        return Response.SYSTEM_SUCCESS();
    }

    /**
     * 评论帖子
     */
    @Override
    @PostMapping("/comment/post")
    public Response<CommentPostResponse> commentDiscussPost(@Valid @RequestBody CommentPostRequest requestDTO) {
        CommentEntity commentEntity = CommentEntity.builder()
                .userId(requestDTO.getUserId())
                .postId(requestDTO.getPostId())
                .content(requestDTO.getContent())
                .images(requestDTO.getImages())
                .build();
        communityService.commentDiscussPost(CommunityBO.builder()
                .commentEntity(commentEntity)
                .build());
        eventPublisher.publishEvent(new CommentPostEvent(CommunityBO.builder()
                .postId(requestDTO.getPostId())
                .build()));
        return Response.SYSTEM_SUCCESS(CommentPostResponse.builder()
                .commentId(commentEntity.getCommentId())
                .build());
    }

    /**
     * 回复评论
     */
    @Override
    @PostMapping("/comment/reply")
    public Response<ReplyCommentResponse> replyComment(@Valid @RequestBody ReplyCommentRequest requestDTO) {
        CommentEntity commentEntity = CommentEntity.builder()
                .userId(requestDTO.getUserId())
                .postId(requestDTO.getPostId())
                .rootId(requestDTO.getRootId())
                .replyTo(requestDTO.getReplyTo())
                .content(requestDTO.getContent())
                .build();
        communityService.replyComment(CommunityBO.builder()
                .commentEntity(commentEntity)
                .build());
        eventPublisher.publishEvent(new ReplyCommentEvent(CommunityBO.builder()
                .postId(requestDTO.getPostId())
                .commentId(requestDTO.getRootId())
                .build()));
        return Response.SYSTEM_SUCCESS(ReplyCommentResponse.builder()
                .commentId(commentEntity.getCommentId())
                .build());
    }

    /**
     * 上传评论图片
     */
    @Override
    @PostMapping("/comment/image")
    public Response<UploadCommentImageResponse> uploadDiscussPostImage(@ModelAttribute UploadCommentImageRequest requestDTO) {
        if (imageOrUserIdIsBlank(requestDTO)) {
            throw new AppException(PARAM_NOT_COMPLETE);
        }
        MultipartFile file = requestDTO.getImage();
        // 使用Thumbnailator进行压缩
        try (InputStream inputStream = file.getInputStream();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Thumbnails.of(inputStream)
                    .scale(1.0) // 设置压缩比例
                    .outputQuality(0.15) // 设置输出质量（0.0到1.0之间）
                    .toOutputStream(outputStream);
            // 将压缩后的图片转换为Base64字符串
            byte[] compressedBytes = outputStream.toByteArray();
            CommunityImage resultImage = communityService.uploadCommentImage(
                    CommunityImage.builder()
                            .userId(requestDTO.getUserId())
                            .image(new CustomMultipartFile(compressedBytes, file.getOriginalFilename()))
                            .build());

            return Response.SYSTEM_SUCCESS(
                    UploadCommentImageResponse.builder()
                            .imageId(resultImage.getImageId())
                            .imageUrl(resultImage.getImageUrl())
                            .build());
        } catch (ClientException e) {
            log.error("上传帖子图片时oss客户端异常，{}, code:{}, message:{}", OSS_UPLOAD_ERROR.getMessage(), e.getErrCode(), e.getErrMsg(), e);
            throw new AppException(OSS_UPLOAD_ERROR, e);
        } catch (Exception e) {
            log.error("上传帖子图片时出现未知异常", e);
            throw new AppException(OSS_UPLOAD_ERROR, e);
        }
    }

    /**
     * 查询帖子详情
     */
    @Override
    @GetMapping("/post/detail")
    public Response<QueryPostDetailResponse> queryPostDetail(QueryPostDetailRequest requestDTO) {
        validatePostId(requestDTO.getPostId());

        CommunityBO communityBO = CommunityBO.builder()
                .userId(requestDTO.getUserId())
                .postId(requestDTO.getPostId())
                .build();
        DiscussPostEntity discussPost = communityService.getDiscussPost(communityBO);
        return Response.SYSTEM_SUCCESS(buildPostDetail(discussPost));
    }

    /**
     * 查询帖子评论列表
     * @param requestDTO
     * @return
     */
    @Override
    @GetMapping("/post/comment")
    public Response<List<QueryPostCommentResponse>> queryPostComment(QueryPostCommentRequest requestDTO) {
        validatePostId(requestDTO.getPostId());

        CommunityBO communityBO = CommunityBO.builder()
                .userId(requestDTO.getUserId())
                .postId(requestDTO.getPostId())
                .commentId(requestDTO.getCommentId())
                .limit(requestDTO.getLimit())
                .sortType(requestDTO.getSortType())
                .build();
        List<CommentEntity> comments = communityService.listPostComment(communityBO);

        List<QueryPostCommentResponse> responseDTOs = new ArrayList<>();
        for (CommentEntity comment : comments) {
            responseDTOs.add(QueryPostCommentResponse.builder()
                    .commentId(comment.getCommentId())
                    .postId(comment.getPostId())
                    .userId(comment.getUserId())
                    .userName(comment.getUserName())
                    .userAvatar(comment.getUserAvatar())
                    .content(comment.getContent())
                    .likeCount(comment.getLikeCount())
                    .replyCount(comment.getReplyCount())
                    .commentTime(comment.getCommentTime())
                    .updateTime(comment.getUpdateTime())
                    .images(comment.getImages())
                    .replyList(comment.getReplyList())
                    .isLike(comment.getIsLike())
                    .build());
        }
        return Response.SYSTEM_SUCCESS(responseDTOs);
    }

    /**
     * 查询评论子评论列表
     */
    @Override
    @GetMapping("/comment/sub")
    public Response<List<QuerySubCommentResponse>> querySubComment(QuerySubCommentRequest requestDTO) {
        validateCommentId(requestDTO.getRootId());

        CommunityBO communityBO = CommunityBO.builder()
                .userId(requestDTO.getUserId())
                .rootId(requestDTO.getRootId())
                .commentId(requestDTO.getCommentId())
                .limit(requestDTO.getLimit())
                .sortType(requestDTO.getSortType())
                .build();
        List<CommentEntity> comments = communityService.listSubComment(communityBO);

        List<QuerySubCommentResponse> responseDTOs = new ArrayList<>();
        for (CommentEntity comment : comments) {
            responseDTOs.add(QuerySubCommentResponse.builder()
                    .commentId(comment.getCommentId())
                    .userId(comment.getUserId())
                    .userName(comment.getUserName())
                    .userAvatar(comment.getUserAvatar())
                    .content(comment.getContent())
                    .replyTo(comment.getReplyTo())
                    .replyToName(comment.getReplyToName())
                    .likeCount(comment.getLikeCount())
                    .commentTime(comment.getCommentTime())
                    .updateTime(comment.getUpdateTime())
                    .isLike(comment.getIsLike())
                    .build());
        }
        return Response.SYSTEM_SUCCESS(responseDTOs);
    }

    /**
     * 点赞评论
     */
    @Override
    @PutMapping("/comment/like")
    public Response likeComment(@Valid @RequestBody LikeCommentRequest requestDTO) {
        CommunityBO communityBO = CommunityBO.builder()
                .userId(requestDTO.getUserId())
                .commentId(requestDTO.getCommentId())
                .eventTime(requestDTO.getLikeTime())
                .build();
        communityService.likeComment(communityBO);
//        eventPublisher.publishEvent(new LikeEvent(CommunityBO.builder()
//                .userId(requestDTO.getUserId())
//                .postId(requestDTO.getPostId())
//                .eventTime(requestDTO.getLikeTime())
//                .build()));
        return Response.SYSTEM_SUCCESS();
    }

    private QueryPostDetailResponse buildPostDetail(DiscussPostEntity discussPost) {
        return QueryPostDetailResponse.builder()
                .userId(discussPost.getUserId())
                .title(discussPost.getTitle())
                .content(discussPost.getContent())
                .tag(discussPost.getTag())
                .likeCount(discussPost.getLikeCount())
                .commentCount(discussPost.getCommentCount())
                .favoriteCount(discussPost.getFavoriteCount())
                .type(discussPost.getType())
                .status(discussPost.getStatus())
                .createTime(discussPost.getCreateTime())
                .updateTime(discussPost.getUpdateTime())
                .images(discussPost.getImages())
                .isFavorite(discussPost.getIsFavorite())
                .isLike(discussPost.getIsLike())
                .build();
    }

    private boolean imageOrUserIdIsBlank(UploadDiscussPostImageRequest requestDTO) {
        return StringUtils.isEmpty(requestDTO.getImage().getOriginalFilename()) || StringUtils.isEmpty(requestDTO.getUserId());
    }

    private boolean imageOrUserIdIsBlank(UploadCommentImageRequest requestDTO) {
        return StringUtils.isEmpty(requestDTO.getImage().getOriginalFilename()) || StringUtils.isEmpty(requestDTO.getUserId());
    }

    private void validateUserId(String userId) {
        if (StringUtils.isEmpty(userId)) {
            throw new AppException(400, "用户id不能为空!");
        }
    }

    private void validatePostId(String postId) {
        if (StringUtils.isEmpty(postId)) {
            throw new AppException(400, "帖子id不能为空!");
        }
    }

    private void validateCommentId(String postId) {
        if (StringUtils.isEmpty(postId)) {
            throw new AppException(400, "评论id不能为空!");
        }
    }
}
