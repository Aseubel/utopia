package com.aseubel.infrastructure.convertor;

import com.aseubel.domain.user.model.entity.AvatarEntity;
import com.aseubel.infrastructure.dao.po.Avatar;
import org.springframework.stereotype.Component;

/**
 * @author Aseubel
 * @description AvatarConvertor
 * @date 2025-01-21 14:29
 */
@Component
public class AvatarConvertor {

    public Avatar convert(AvatarEntity avatarEntity) {
        return Avatar.builder()
                .avatarId(avatarEntity.getAvatarId())
                .avatarUrl(avatarEntity.getAvatarUrl())
                .userId(avatarEntity.getUserId())
                .build();
    }

}
