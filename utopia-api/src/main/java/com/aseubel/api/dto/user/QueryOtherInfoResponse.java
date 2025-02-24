package com.aseubel.api.dto.user;

import com.aseubel.types.annotation.FieldDesc;
import lombok.*;

import java.io.Serializable;

/**
 * @author Aseubel
 * @description 查询他人信息
 * @date 2025-02-24 12:06
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QueryOtherInfoResponse implements Serializable {

    @FieldDesc(name = "用户id")
    private String userId;

    @FieldDesc(name = "用户名")
    private String userName;

    @FieldDesc(name = "头像url")
    private String avatarUrl;

    @FieldDesc(name = "学校代码")
    private String schoolCode;

    @FieldDesc(name = "性别，0-隐藏，1-男，2-女")
    private Integer gender;

    @FieldDesc(name = "个性签名")
    private String signature;

}
