package com.aseubel.infrastructure.convertor;

import com.aseubel.domain.user.model.UserEntity;
import com.aseubel.infrastructure.dao.po.User;
import org.springframework.stereotype.Component;

/**
 * @author Aseubel
 * @description 用户实体类转换器
 * @date 2025-01-12 12:37
 */
@Component
public class UserConvertor {

    public User convert(UserEntity userEntity) {
        return User.builder()
                .userId(userEntity.getUserId())
                .userName(userEntity.getUserName())
                .phone(userEntity.getPhone())
                .gender(userEntity.getGender())
                .realName(userEntity.getRealName())
                .avatar(userEntity.getAvatar())
                .signature(userEntity.getSignature())
                .build();
    }

    public UserEntity convert(User user) {
        return UserEntity.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .phone(user.getPhone())
                .gender(user.getGender())
                .realName(user.getRealName())
                .avatar(user.getAvatar())
                .signature(user.getSignature())
                .build();
    }

}
