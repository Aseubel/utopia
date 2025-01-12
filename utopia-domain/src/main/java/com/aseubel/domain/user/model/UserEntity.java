package com.aseubel.domain.user.model;

import com.aseubel.types.annotation.FieldDesc;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Aseubel
 * @description 领域层用户实体类
 * @date 2025-01-12 12:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEntity {

    @FieldDesc(name = "用户id")
    private String openid;

    @FieldDesc(name = "用户名")
    private String userName;

    @FieldDesc(name = "真实姓名")
    private String realName;

    @FieldDesc(name = "手机号码")
    private String phone;

    @FieldDesc(name = "性别，0-隐藏，1-男，2-女")
    private Integer gender;

    @FieldDesc(name = "头像")
    private String avatar;

    @FieldDesc(name = "个性签名")
    private String signature;

}
