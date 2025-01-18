package com.aseubel.api.dto.user;

import com.aseubel.types.annotation.FieldDesc;
import lombok.*;

import java.io.Serializable;

/**
 * @author Aseubel
 * @description 修改用户信息请求DTO，携带用户信息
 * @date 2025-01-18 11:07
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ModifyUserInfoRequestDTO implements Serializable {

    @FieldDesc(name = "用户id")
    private String userId;

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
