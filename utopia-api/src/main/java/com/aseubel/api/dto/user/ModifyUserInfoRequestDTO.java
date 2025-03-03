package com.aseubel.api.dto.user;

import com.aseubel.types.annotation.FieldDesc;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "用户id不能为空")
    @FieldDesc(name = "用户id")
    private String userId;

    @NotNull(message = "用户名不能为空")
    @FieldDesc(name = "用户名")
    private String userName;

//    @Pattern(regexp = "^[0-9]{5}$", message = "学校代码格式不正确")
    @FieldDesc(name = "学校代码")
    private String schoolCode;

    @FieldDesc(name = "真实姓名")
    private String realName;

//    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号码格式不正确")
    @FieldDesc(name = "手机号码")
    private String phone;

    @NotNull(message = "性别不能为空")
    @Min(value = 0, message = "性别必须为0、1或2")
    @Max(value = 2, message = "性别必须为0、1或2")
    @FieldDesc(name = "性别，0-隐藏，1-男，2-女")
    private Integer gender;

    @FieldDesc(name = "头像")
    private String avatar;

    @FieldDesc(name = "个性签名")
    private String signature;

}
