package com.aseubel.infrastructure.adapter.repo;

import com.aseubel.domain.user.adapter.repo.IAvatarRepository;
import com.aseubel.domain.user.model.entity.AvatarEntity;
import com.aseubel.infrastructure.convertor.AvatarConvertor;
import com.aseubel.infrastructure.dao.AvatarMapper;
import com.aseubel.infrastructure.dao.po.Avatar;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @author Aseubel
 * @description AvatarRepository
 * @date 2025-01-21 14:26
 */
@Repository
public class AvatarRepository implements IAvatarRepository {

    @Resource
    private AvatarMapper avatarMapper;

    @Resource
    private AvatarConvertor avatarConvertor;

    public void saveAvatar(AvatarEntity avatar) {
        avatarMapper.addAvatar(avatarConvertor.convert(avatar));
    }

}
