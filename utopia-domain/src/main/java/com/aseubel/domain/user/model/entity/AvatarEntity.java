package com.aseubel.domain.user.model.entity;

import com.aseubel.types.annotation.FieldDesc;
import com.aseubel.types.exception.AppException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

import static com.aseubel.types.common.Constant.APP;
import static com.aseubel.types.common.Constant.AVATAR;

/**
 * @author Aseubel
 * @description 头像ValuableObject
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AvatarEntity {

    @FieldDesc(name = "头像文件本体")
    private MultipartFile avatar;

    @FieldDesc(name = "avatar_id")
    private String avatarId;

    @FieldDesc(name = "用户id")
    private String userId;

    @FieldDesc(name = "avatar_url")
    private String avatarUrl;

    /**
     * 获取在OSS中的对象名称（在类型文件夹下）
     */
    public String getObjectName() {
        if (StringUtils.isEmpty(userId)) {
            throw new AppException("头像的用户id不能为空");
        }
        StringBuilder objectName = new StringBuilder();
        objectName.append(APP).append("/").append(AVATAR).append("/").append(avatarId);
        return objectName.toString();
    }

    /**
     * 生成头像id
     */
    public void generateAvatarId() {
        this.avatarId = LocalDateTime.now().toString().replace("-", "").replace(":", "").replace(".", "") + "_" + userId;
    }

}
