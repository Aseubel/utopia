package com.aseubel.domain.user.service;

import com.aliyuncs.exceptions.ClientException;
import com.aseubel.domain.user.adapter.repo.IAdminRepository;
import com.aseubel.domain.user.adapter.repo.IAvatarRepository;
import com.aseubel.domain.user.adapter.repo.IUserRepository;
import com.aseubel.domain.user.adapter.wx.WxService;
import com.aseubel.domain.user.model.entity.AvatarEntity;
import com.aseubel.domain.user.model.entity.UserEntity;
import com.aseubel.types.exception.AppException;
import com.aseubel.types.util.AliOSSUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

import static com.aseubel.types.common.Constant.USER_ID_KEY;

/**
 * @author Aseubel
 * @description 用户领域服务实现类
 * @date 2025-01-12 17:25
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final AliOSSUtil aliOSSUtil;

    private final IUserRepository userRepository;

    private final IAvatarRepository avatarRepository;

    private final IAdminRepository adminRepository;

    private final WxService wxService;

    @Value("${jwt.config.secret-key:aseubel-secret-key}")
    private String secretKey;

    @Value("${jwt.config.refresh_ttl}")
    private Long refreshTtl;

    @Value("${jwt.config.access_ttl}")
    private Long accessTtl;

    @Value("${wechat.config.appid:aseubel-appid}")
    private String appid;

    @Value("${wechat.config.secret:aseubel-secret}")
    private String secret;

    @Override
    public UserEntity login(String code) {
        String openid = code; // wxService.getOpenid(appid, secret, code);

        //当前用户为新用户,完成自动注册
        UserEntity user = userRepository.queryUserInfo(openid);
        if (user == null) {
            user = UserEntity.builder().openid(openid).build();
            userRepository.addUser(user);
        }
        if (user.getSchool() != null) {
            user.setIsAdmin(adminRepository.isAdmin(user.getOpenid(), user.getSchool().getSchoolCode()));
        }

        //生成JWT令牌
        Map<String, Object> claims=new HashMap<>();
        claims.put(USER_ID_KEY,user.getOpenid());
        user.generateToken(secretKey, refreshTtl, accessTtl, claims);

        // 记录token
        userRepository.saveUserToken(user);

        return user;
    }

    @Override
    public void logout(String openid) {
        userRepository.cleanUserToken(openid);
    }

    @Override
    public UserEntity queryUserInfo(String openid) {
        UserEntity user = userRepository.queryUserInfo(openid);
        if (user.getSchool() != null) {
            user.setIsAdmin(adminRepository.isAdmin(user.getOpenid(), user.getSchool().getSchoolCode()));
        }
        return user;
    }

    @Override
    public UserEntity queryOtherInfo(String userId, String targetId) {
        UserEntity user = userRepository.queryOtherInfo(targetId);
        return user;
    }

    @Override
    public UserEntity refreshToken(UserEntity user) {
        if (!userRepository.checkRefreshToken(user, secretKey)) {
            log.error("refreshToken无效！, user={}", user);
            throw new AppException("refreshToken无效！");
        }
        user.generateToken(secretKey, refreshTtl, accessTtl, new HashMap<>());

        // 记录token
        userRepository.saveUserToken(user);

        return user;
    }

    @Override
    public void updateUserInfo(UserEntity user) {
        checkUserIdValid(user.getOpenid());
        checkSchoolCodeValid(user.getSchool().getSchoolCode());
        userRepository.saveUserInfo(user);
    }

    @Override
    public String uploadAvatar(AvatarEntity avatar) throws ClientException {
        avatar.generateAvatarId();
        MultipartFile file = avatar.getAvatar();
        // 获取文件名不包含扩展名
        String ossUrl = aliOSSUtil.upload(file, avatar.getObjectName());
        avatar.setAvatarUrl(ossUrl);

        avatarRepository.saveAvatar(avatar);
        return ossUrl;
    }

    @Override
    public void cancelAccount(String openid) {
        log.info("注销账户服务开始执行，openid={}", openid);
        userRepository.deleteUser(openid);
        userRepository.cleanUserToken(openid);
        log.info("注销账户服务结束执行，openid={}", openid);
    }

    private void checkSchoolCodeValid(String schoolCode) {
        if (!StringUtils.isEmpty(schoolCode) && !userRepository.isSchoolCodeValid(schoolCode)) {
            log.error("学校代号无效！, user={}", schoolCode);
            throw new AppException("学校代号无效！");
        }
    }

    private void checkUserIdValid(String userId) {
        if (StringUtils.isEmpty(userId) || !userRepository.isUserIdValid(userId)) {
            log.error("用户id无效！, user={}", userId);
            throw new AppException("用户id无效！");
        }
    }
}
