package com.aseubel.infrastructure.dao.po;

import com.aseubel.types.annotation.FieldDesc;
import lombok.*;

import java.time.LocalDateTime;

/**
 * @author Aseubel
 * @description 用户实体类
 * @date 2025-01-12 12:15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @FieldDesc(name = "主键id")
    private Long id;

    @FieldDesc(name = "用户id")
    private String userId;

    @FieldDesc(name = "用户名")
    private String userName;

    @FieldDesc(name = "真实姓名")
    private String realName;

    @FieldDesc(name = "学校代号")
    private String schoolCode;

    @FieldDesc(name = "手机号码")
    private String phone;

    @FieldDesc(name = "性别，0-隐藏，1-男，2-女")
    private Integer gender;

    @FieldDesc(name = "头像")
    private String avatar;

    @FieldDesc(name = "个性签名")
    private String signature;

    @FieldDesc(name = "注册时间")
    private LocalDateTime createTime;

    @FieldDesc(name = "更新时间")
    private LocalDateTime updateTime;

}
