package com.aseubel.trigger.http;

import com.aseubel.api.BazaarInterface;
import com.aseubel.api.dto.bazaar.*;
import com.aseubel.domain.bazaar.model.bo.BazaarBO;
import com.aseubel.domain.bazaar.model.entity.TradeImage;
import com.aseubel.domain.bazaar.model.entity.TradePostEntity;
import com.aseubel.domain.bazaar.service.IBazaarService;
import com.aseubel.domain.community.model.bo.CommunityBO;
import com.aseubel.types.Response;
import com.aseubel.types.exception.AppException;
import com.aseubel.types.util.CustomMultipartFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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

    /**
     * 查询首页帖子列表
     */
    @Override
    @GetMapping("/post")
    public Response<List<QueryIndexTradePostResponse>> queryIndexTradePost(@Valid QueryIndexTradePostRequest requestDTO) {
        List<TradePostEntity> tradePosts = bazaarService.listTradePost(BazaarBO.builder()
                .userId(requestDTO.getUserId())
                .postId(requestDTO.getPostId())
                .limit(requestDTO.getLimit())
                .type(requestDTO.getType())
                .status(requestDTO.getStatus())
                .build());
        List<QueryIndexTradePostResponse> responseDTOs = new ArrayList<>();
        for (TradePostEntity tradePost : tradePosts) {
           responseDTOs.add(QueryIndexTradePostResponse.builder()
                   .tradePostId(tradePost.getTradePostId())
                   .userId(tradePost.getUserId())
                   .userName(tradePost.getUserName())
                   .userAvatar(tradePost.getUserAvatar())
                   .title(tradePost.getTitle())
                   .content(tradePost.getContent())
                   .price(tradePost.getPrice())
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
        // 使用CompletableFuture进行异步处理
        CompletableFuture<TradeImage> futureImage = CompletableFuture.supplyAsync(() -> {
            try (InputStream inputStream = file.getInputStream();
                 ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                BufferedImage image = ImageIO.read(inputStream);
                if (image == null) {
                    throw new AppException("上传的图片格式不正确");
                }
                // 获取JPEG ImageWriter
                Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
                if (!writers.hasNext()) {
                    throw new AppException("没有可用的JPEG ImageWriter");
                }
                ImageWriter writer = writers.next();
                ImageWriteParam param = writer.getDefaultWriteParam();
                param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                param.setCompressionQuality(0.3f); // 设置压缩质量

                ImageOutputStream ios = ImageIO.createImageOutputStream(outputStream);
                writer.setOutput(ios);
                writer.write(null, new javax.imageio.IIOImage(image, null, null), param);
                writer.dispose();
                ios.close();

                // 将压缩后的图片转换为字节数组
                byte[] compressedBytes = outputStream.toByteArray();

                // 使用压缩后的图片进行上传
                return bazaarService.uploadPostImage(
                        TradeImage.builder()
                                .userId(requestDTO.getUserId())
                                .image(new CustomMultipartFile(compressedBytes, file.getOriginalFilename()))
                                .build());
            } catch (IOException e) {
                log.error("图片压缩时出现IO异常", e);
                throw new AppException(OSS_UPLOAD_ERROR, e);
            } catch (Exception e) {
                log.error("上传帖子图片时出现未知异常", e);
                throw new AppException(OSS_UPLOAD_ERROR, e);
            }
        });

        // 等待异步任务完成
        TradeImage resultImage;
        try {
            resultImage = futureImage.get();
        } catch (Exception e) {
            log.error("异步图片压缩时出现异常", e);
            throw new AppException(OSS_UPLOAD_ERROR, e);
        }

        return Response.SYSTEM_SUCCESS(
                UploadTradePostImageResponse.builder()
                        .imageId(resultImage.getImageId())
                        .imageUrl(resultImage.getImageUrl())
                        .build());
    }


    /**
     * 发布帖子
     */
    @Override
    @PostMapping("/post")
    public Response publishTradePost(@Valid @RequestBody PublishTradePostRequest publishTradePostRequest) {
        TradePostEntity tradePostEntity = TradePostEntity.builder()
               .userId(publishTradePostRequest.getUserId())
               .title(publishTradePostRequest.getTitle())
               .content(publishTradePostRequest.getContent())
               .tags(publishTradePostRequest.getTags())
                .type(publishTradePostRequest.getType())
                .price(publishTradePostRequest.getPrice())
               .images(publishTradePostRequest.getImages())
               .build();
        bazaarService.publishTradePost(tradePostEntity);
        return Response.SYSTEM_SUCCESS();
    }

    private boolean imageOrUserIdIsBlank(UploadTradePostImageRequest requestDTO) {
        return StringUtils.isEmpty(requestDTO.getPostImage().getOriginalFilename()) || StringUtils.isEmpty(requestDTO.getUserId());
    }

}
