package com.aseubel.trigger.http;

import com.aliyuncs.exceptions.ClientException;
import com.aseubel.api.CommunityInterface;
import com.aseubel.api.dto.community.*;
import com.aseubel.domain.community.model.entity.CommunityImage;
import com.aseubel.domain.community.model.entity.DiscussPostEntity;
import com.aseubel.domain.community.service.ICommunityService;
import com.aseubel.types.Response;
import com.aseubel.types.exception.AppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static com.aseubel.types.enums.GlobalServiceStatusCode.OSS_UPLOAD_ERROR;
import static com.aseubel.types.enums.GlobalServiceStatusCode.PARAM_NOT_COMPLETE;

@Slf4j
@RestController()
@Validated
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/v1/community/") //${app.config.api-version}
@RequiredArgsConstructor
public class CommunityController implements CommunityInterface {

    private final ICommunityService communityService;

    /**
     * 查询首页帖子列表
     */
    @Override
    @GetMapping("/post")
    public Response<List<QueryIndexDiscussPostResponseDTO>> queryIndexDiscussPost(@Valid QueryIndexDiscussPostRequestDTO requestDTO) {
        List<DiscussPostEntity> discussPosts = communityService.listDiscussPost(requestDTO.getPostId(), requestDTO.getLimit());
        List<QueryIndexDiscussPostResponseDTO> responseDTOs = new ArrayList<>();
        for (DiscussPostEntity discussPost : discussPosts) {
           responseDTOs.add(QueryIndexDiscussPostResponseDTO.builder()
                   .discussPostId(discussPost.getDiscussPostId())
                   .userId(discussPost.getUserId())
                   .userName(discussPost.getUserName())
                   .userAvatar(discussPost.getUserAvatar())
                   .title(discussPost.getTitle())
                   .content(discussPost.getContent())
                   .likeCount(discussPost.getLikeCount())
                   .commentCount(discussPost.getCommentCount())
                   .forwardCount(discussPost.getForwardCount())
                   .type(discussPost.getType())
                   .status(discussPost.getStatus())
                   .createTime(discussPost.getCreateTime())
                   .updateTime(discussPost.getUpdateTime())
                   .image(discussPost.getImage())
                   .build());
        }
        return Response.SYSTEM_SUCCESS(responseDTOs);
    }

    /**
     * 上传帖子图片
     */
    @Override
    @PostMapping("/post/image")
    public Response<UploadDiscussPostImageResponse> uploadDiscussPostImage(@ModelAttribute UploadDiscussPostImageRequest requestDTO) {
       try {
           if (imageOrUserIdIsBlank(requestDTO)) {
               throw new AppException(PARAM_NOT_COMPLETE);
           }
           CommunityImage image = communityService.uploadPostImage(
                   CommunityImage.builder()
                           .userId(requestDTO.getUserId())
                           .image(requestDTO.getPostImage())
                           .build());

           return Response.SYSTEM_SUCCESS(
                   UploadDiscussPostImageResponse.builder()
                   .imageId(image.getImageId())
                   .imageUrl(image.getImageUrl())
                   .build());
       } catch (ClientException e) {
           log.error("上传帖子图片时oss客户端异常，{}, code:{}, message:{}",OSS_UPLOAD_ERROR.getMessage(), e.getErrCode(), e.getErrMsg(), e);
           throw new AppException(OSS_UPLOAD_ERROR, e);
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
    public Response publishDiscussPost(@Valid @RequestBody PublishDiscussPostRequest publishDiscussPostRequest) {
        DiscussPostEntity discussPostEntity = DiscussPostEntity.builder()
               .userId(publishDiscussPostRequest.getUserId())
               .title(publishDiscussPostRequest.getTitle())
               .content(publishDiscussPostRequest.getContent())
               .tags(publishDiscussPostRequest.getTags())
               .images(publishDiscussPostRequest.getImages())
               .build();
        communityService.publishDiscussPost(discussPostEntity);
        return Response.SYSTEM_SUCCESS();
    }

    private boolean imageOrUserIdIsBlank(UploadDiscussPostImageRequest requestDTO) {
        return StringUtils.isEmpty(requestDTO.getPostImage().getOriginalFilename()) || StringUtils.isEmpty(requestDTO.getUserId());
    }

}
