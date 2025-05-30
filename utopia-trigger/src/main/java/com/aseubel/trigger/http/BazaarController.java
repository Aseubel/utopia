package com.aseubel.trigger.http;

import cn.hutool.core.collection.CollectionUtil;
import com.aliyuncs.exceptions.ClientException;
import com.aseubel.api.BazaarInterface;
import com.aseubel.api.dto.bazaar.*;
import com.aseubel.domain.bazaar.model.bo.BazaarBO;
import com.aseubel.domain.bazaar.model.entity.TradeImage;
import com.aseubel.domain.bazaar.model.entity.TradePostEntity;
import com.aseubel.domain.bazaar.service.IBazaarService;
import com.aseubel.domain.community.model.entity.DiscussPostEntity;
import com.aseubel.types.Response;
import com.aseubel.types.constraint.Auth;
import com.aseubel.types.event.*;
import com.aseubel.types.exception.AppException;
import com.aseubel.types.util.CustomMultipartFile;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.aseubel.types.enums.GlobalServiceStatusCode.OSS_UPLOAD_ERROR;
import static com.aseubel.types.enums.GlobalServiceStatusCode.PARAM_NOT_COMPLETE;

/**
 * 集市
 */
@Slf4j
@RestController()
@Validated
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/v1/bazaar/") //${app.config.api-version}
@RequiredArgsConstructor
public class BazaarController implements BazaarInterface {

    private final IBazaarService bazaarService;

    private final ApplicationEventPublisher eventPublisher;

    /**
     * 查询首页帖子列表
     */
    @Override
    @GetMapping("/post")
    public Response<List<QueryIndexTradePostResponse>> queryIndexTradePost(QueryIndexTradePostRequest requestDTO) {
        List<TradePostEntity> tradePosts = bazaarService.listTradePost(BazaarBO.builder()
                .userId(requestDTO.getUserId())
                .postId(requestDTO.getPostId())
                .limit(requestDTO.getLimit())
                .type(requestDTO.getType())
                .status(requestDTO.getStatus())
                .schoolCode(requestDTO.getSchoolCode())
                .build());
        List<QueryIndexTradePostResponse> responseDTOs = new ArrayList<>();
        for (TradePostEntity tradePost : tradePosts) {
            responseDTOs.add(QueryIndexTradePostResponse.builder()
                    .tradePostId(tradePost.getPostId())
                    .userId(tradePost.getUserId())
                    .userName(tradePost.getUserName())
                    .userAvatar(tradePost.getUserAvatar())
                    .title(tradePost.getTitle())
                    .content(tradePost.getContent())
                    .price(tradePost.getPrice())
                    .contact(tradePost.getContact())
                    .schoolCode(tradePost.getSchoolCode())
                    .type(tradePost.getType())
                    .status(tradePost.getStatus())
                    .createTime(tradePost.getCreateTime())
                    .updateTime(tradePost.getUpdateTime())
                    .image(tradePost.getImage())
                    .build());
        }
        return Response.SYSTEM_SUCCESS(responseDTOs);
    }

    /**
     * 上传帖子图片
     */
    @Override
    @PostMapping("/post/image")
    public Response<UploadTradePostImageResponse> uploadTradePostImage(@ModelAttribute UploadTradePostImageRequest requestDTO) {
        if (imageOrUserIdIsBlank(requestDTO)) {
            throw new AppException(PARAM_NOT_COMPLETE);
        }
        MultipartFile file = requestDTO.getPostImage();
        // 使用Thumbnailator进行压缩
        try (InputStream inputStream = new BufferedInputStream(file.getInputStream());
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
            TradeImage resultImage = bazaarService.uploadPostImage(
                    TradeImage.builder()
                            .userId(requestDTO.getUserId())
                            .image(new CustomMultipartFile(compressedBytes, file.getOriginalFilename()))
                            .build());

            return Response.SYSTEM_SUCCESS(
                    UploadTradePostImageResponse.builder()
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
    public Response<PublishPostResponse> publishTradePost(@Valid @RequestBody PublishTradePostRequest publishTradePostRequest) {
        TradePostEntity post = TradePostEntity.builder()
                .userId(publishTradePostRequest.getUserId())
                .title(publishTradePostRequest.getTitle())
                .content(publishTradePostRequest.getContent())
                .tags(publishTradePostRequest.getTags())
                .type(publishTradePostRequest.getType())
                .price(publishTradePostRequest.getPrice())
                .contact(publishTradePostRequest.getContact())
                .schoolCode(publishTradePostRequest.getSchoolCode())
                .images(publishTradePostRequest.getImages())
                .build();

        String postId = bazaarService.publishTradePost(post);
        eventPublisher.publishEvent(new PublishTradePostEvent("publishDiscussPost",
                post.getUserId(), postId, post.getPrice(), post.getTitle(),
                post.getContent(), getFirstImage(post), post.getType(), post.getSchoolCode()));
        eventPublisher.publishEvent(new PostAuditEvent("bazaarPostAudit", post.getUserId(), post.getPostId(), post.getSchoolCode(), post.getTitle(), post.getContent(), post.getImages()));
        return Response.SYSTEM_SUCCESS(new PublishPostResponse(postId));
    }

    /**
     * 查询帖子详情
     * @param requestDTO
     * @return
     */
    @Override
    @GetMapping("/post/detail")
    public Response<QueryPostDetailResponse> queryPostDetail(QueryPostDetailRequest requestDTO) {
        TradePostEntity tradePost = bazaarService.queryPostDetail(requestDTO.getPostId());
        return Response.SYSTEM_SUCCESS(QueryPostDetailResponse.builder()
                    .tradePostId(tradePost.getPostId())
                    .userId(tradePost.getUserId())
                    .userName(tradePost.getUserName())
                    .userAvatar(tradePost.getUserAvatar())
                    .title(tradePost.getTitle())
                    .content(tradePost.getContent())
                    .price(tradePost.getPrice())
                    .contact(tradePost.getContact())
                    .schoolCode(tradePost.getSchoolCode())
                    .type(tradePost.getType())
                    .status(tradePost.getStatus())
                    .createTime(tradePost.getCreateTime())
                    .updateTime(tradePost.getUpdateTime())
                    .images(tradePost.getImages())
                    .build());
    }

    /**
     * 删除帖子
     */
    @Override
    @DeleteMapping("/post")
    public Response deleteTradePost(@Valid @RequestBody DeletePostRequest requestDTO) {
        BazaarBO bazaarBO = BazaarBO.builder()
                .userId(requestDTO.getUserId())
                .postId(requestDTO.getPostId())
                .schoolCode(requestDTO.getSchoolCode())
                .build();
        bazaarService.deletePost(bazaarBO);
//        eventPublisher.publishEvent(new DeleteTradePostEvent("deleteTradePost", requestDTO.getUserId(), requestDTO.getPostId(), requestDTO.getSchoolCode()));
        return Response.SYSTEM_SUCCESS();
    }

    /**
     * 完成交易(修改帖子状态已完成)
     */
    @Override
    @PutMapping("/post/complete")
    public Response CompleteTrade(@Valid @RequestBody CompleteTradeRequest requestDTO) {
        BazaarBO bazaarBO = BazaarBO.builder()
                .userId(requestDTO.getUserId())
                .postId(requestDTO.getPostId())
                .build();
        bazaarService.completeTrade(bazaarBO);
//        eventPublisher.publishEvent(new CompleteTradeEvent("completeTrade", requestDTO.getUserId(), requestDTO.getPostId(), requestDTO.getSchoolCode()));
        return Response.SYSTEM_SUCCESS();
    }

    private boolean imageOrUserIdIsBlank(UploadTradePostImageRequest requestDTO) {
        return StringUtils.isEmpty(requestDTO.getPostImage().getOriginalFilename()) || StringUtils.isEmpty(requestDTO.getUserId());
    }

    private String getFirstImage(TradePostEntity post) {
        List<String> images = post.getImages();
        if (CollectionUtil.isEmpty(images)) {
            return "";
        } else {
            return images.get(0);
        }
    }

}
