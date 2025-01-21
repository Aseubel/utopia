package com.aseubel.domain.user.adapter.repo;

import com.aseubel.domain.user.model.entity.AvatarEntity;

/**
 * @author Aseubel
 * @description Interface for Avatar Repository
 * @date 2025-01-21 14:28
 */
public interface IAvatarRepository {

    /**
     * 保存图片信息到数据库
     * @param avatar
     */
    void saveAvatar(AvatarEntity avatar);

}
