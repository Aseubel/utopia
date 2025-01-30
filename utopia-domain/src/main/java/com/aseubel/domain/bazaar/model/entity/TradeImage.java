package com.aseubel.domain.bazaar.model.entity;

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
 * @description 集市区图片
 * @date 2025-01-22 12:04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TradeImage {

    @FieldDesc(name = "图片本体")
    private MultipartFile image;

    @FieldDesc(name = "图片id")
    private String imageId;

    @FieldDesc(name = "图片url")
    private String imageUrl;

    @FieldDesc(name = "帖子id")
    private String postId;

    @FieldDesc(name = "用户id")
    private String userId;

    /**
     * 获取在OSS中的文件名称（在类型文件夹下）
     */
    public String getObjectName() {
        if (StringUtils.isEmpty(userId)) {
            throw new AppException("图片用户id不能为空");
        }
        StringBuilder objectName = new StringBuilder();
        objectName.append(APP).append("/")
                .append(TRADE_POST_IMAGE).append("/")
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
