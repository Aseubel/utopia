package com.aseubel.domain.community.model.entity;

import com.aseubel.types.annotation.FieldDesc;
import com.aseubel.types.exception.AppException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import static com.aseubel.types.common.Constant.*;

/**
 * @author Aseubel
 * @description 社区帖子、评论图片
 * @date 2025-01-21 20:46
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommunityImage {

    @FieldDesc(name = "图片本体")
    private MultipartFile image;

    @FieldDesc(name = "图片id")
    private String imageId;

    @FieldDesc(name = "图片url")
    private String imageUrl;

    @FieldDesc(name = "帖子id")
    private String postId;

    @FieldDesc(name = "评论id")
    private String commentId;

    @FieldDesc(name = "用户id")
    private String userId;

    /**
     * 获取在OSS中的文件名称（在类型文件夹下）
     */
    public String getPostObjectName() {
        if (StringUtils.isEmpty(userId)) {
            throw new AppException("图片用户id不能为空");
        }
        StringBuilder objectName = new StringBuilder();
        objectName.append(APP).append("/")
                .append(DISCUSS_POST_IMAGE).append("/")
                .append(imageId)
                .append(Objects.requireNonNull(image.getOriginalFilename()).substring(image.getOriginalFilename().lastIndexOf(".")));
        return objectName.toString();
    }

    public String getCommentObjectName() {
        if (StringUtils.isEmpty(userId)) {
            throw new AppException("图片用户id不能为空");
        }
        StringBuilder objectName = new StringBuilder();
        objectName.append(APP).append("/")
                .append(COMMENT_IMAGE).append("/")
                .append(imageId)
                .append(Objects.requireNonNull(image.getOriginalFilename()).substring(image.getOriginalFilename().lastIndexOf(".")));
        return objectName.toString();
    }

    /**
     * 生成图片id
     */
    public void generateImageId() {
        this.imageId = "img_" + UUID.randomUUID().toString().replaceAll("-", "");
    }
    
}
