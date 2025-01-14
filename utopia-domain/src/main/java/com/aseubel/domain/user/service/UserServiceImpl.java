package com.aseubel.domain.user.service;

import com.aseubel.domain.user.adapter.repo.IUserRepository;
import com.aseubel.domain.user.adapter.wx.WxService;
import com.aseubel.domain.user.model.UserEntity;
import com.aseubel.types.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Aseubel
 * @description 用户领域服务实现类
 * @date 2025-01-12 17:25
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final IUserRepository userRepository;

    private final WxService wxService;

    private final JwtUtil jwtUtil;

    @Value("${jwt.config.secret-key:aseubel-secret-key}")
    private String secretKey;

    @Value("${jwt.config.ttl}")
    private Long ttl;

    @Value("${wechat.config.appid:aseubel-appid}")
    private String appid;

    @Value("${wechat.config.secret:aseubel-secret}")
    private String secret;

    @Override
    public UserEntity login(String code) {
        log.info("登录服务开始执行，code={}", code);

        log.info("获取微信用户的openid");
        String openid = wxService.getOpenid(appid, secret, code);

        //当前用户为新用户,完成自动注册
        log.info("查询用户信息, openid={}", openid);
        UserEntity user = userRepository.queryUserInfo(openid);
        if (user == null) {
            user = UserEntity.builder().openid(openid).build();
            userRepository.addUser(user);
        }

        //生成JWT令牌
        Map<String, Object> claims=new HashMap<>();
        claims.put("openid",user.getOpenid());
        String jwt = jwtUtil.createJWT(secretKey, ttl, claims);
        user.setToken(jwt);

        log.info("登录服务结束执行，user={}", openid);
        return user;
    }

    @Override
    public UserEntity queryUserInfo(String openid) {
        log.info("查询个人信息服务开始执行，openid={}", openid);
        UserEntity user = userRepository.queryUserInfo(openid);
        log.info("查询个人信息服务结束执行，user={}", user);
        return user;
    }

}
