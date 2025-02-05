package com.aseubel.domain.user.model.entity;

import com.aseubel.domain.user.model.vo.School;
import com.aseubel.types.annotation.FieldDesc;
import com.aseubel.types.util.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

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

    @FieldDesc(name = "学校代号")
    private School school;

    @FieldDesc(name = "手机号码")
    private String phone;

    @FieldDesc(name = "性别，0-隐藏，1-男，2-女")
    private Integer gender;

    @FieldDesc(name = "头像")
    private String avatar;

    @FieldDesc(name = "个性签名")
    private String signature;

    @FieldDesc(name = "refresh_token")
    private String refreshToken;

    @FieldDesc(name = "access_token")
    private String accessToken;

    /**
     * 生成token
     */
    public void generateToken(String secretKey, Long refreshTokenTtl, Long accessTokenTtl, Map<String, Object> claims) {
        this.refreshToken = JwtUtil.createJWT(secretKey, refreshTokenTtl, claims);
        this.accessToken = JwtUtil.createJWT(secretKey, accessTokenTtl, claims);
    }

}
