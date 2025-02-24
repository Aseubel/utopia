package com.aseubel.infrastructure.adapter.repo;

import com.aseubel.domain.bazaar.adapter.repo.IBazaarUserRepository;
import com.aseubel.domain.community.adapter.repo.ICommunityUserRepository;
import com.aseubel.domain.user.adapter.repo.IUserRepository;
import com.aseubel.domain.user.model.entity.UserEntity;
import com.aseubel.domain.user.model.vo.School;
import com.aseubel.infrastructure.convertor.UserConvertor;
import com.aseubel.infrastructure.dao.SchoolMapper;
import com.aseubel.infrastructure.dao.UserMapper;
import com.aseubel.infrastructure.redis.IRedisService;
import com.aseubel.types.exception.AppException;
import com.aseubel.types.util.JwtUtil;
import com.aseubel.types.util.RedisKeyBuilder;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.aseubel.types.common.Constant.*;

/**
 * @author Aseubel
 * @description 用户数据访问层实现类
 * @date 2025-01-12 17:37
 */
@Repository
@Slf4j
public class UserRepository implements IUserRepository, ICommunityUserRepository, IBazaarUserRepository {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserConvertor userConvertor;

    @Resource
    private IRedisService redisService;

    @Resource
    private SchoolMapper schoolMapper;

    @Override
    public UserEntity queryUserInfo(String userId) {
        return Optional.ofNullable(userMapper.getUserByUserId(userId))
                .map(user -> {
                    UserEntity userEntity = userConvertor.convert(user);
                    userEntity.setSchool(schoolMapper.getSchoolBySchoolCode(user.getSchoolCode()));
                    return userEntity;
                })
                .orElse(null);
    }

    @Override
    public UserEntity queryOtherInfo(String userId) {
        return Optional.ofNullable(userMapper.getOtherInfoByUserId(userId))
                .map(user -> {
                    UserEntity userEntity = userConvertor.convert(user);
                    userEntity.setSchool(schoolMapper.getSchoolBySchoolCode(user.getSchoolCode()));
                    return userEntity;
                })
                .orElse(null);
    }

    @Override
    public void saveUserInfo(UserEntity userEntity) {
        userMapper.updateUser(userConvertor.convert(userEntity));
    }

    @Override
    public void addUser(UserEntity userEntity) {
        userMapper.addUser(userConvertor.convert(userEntity));
    }

    @Override
    public String generateUserToken(String userId, String secretKey, Long ttl) {
        Map<String, Object> claims=new HashMap<>();
        claims.put(USER_ID_KEY, userId);
        return JwtUtil.createJWT(secretKey, ttl, claims);
    }

    @Override
    public void saveUserToken(UserEntity user) {
        String tokenKey = RedisKeyBuilder.UserTokenKey(user.getOpenid());
        redisService.addToMap(tokenKey, ACCESS_TOKEN, user.getAccessToken());
        redisService.addToMap(tokenKey, REFRESH_TOKEN, user.getRefreshToken());
    }

    @Override
    public void cleanUserToken(String openid) {
        redisService.remove(RedisKeyBuilder.UserTokenKey(openid));
    }

    @Override
    public boolean checkRefreshToken(UserEntity user, String secretKey) {
        String refreshToken = user.getRefreshToken();
        String userId = user.getOpenid();
        String token = null;
        try {
            // 校验redis中是否有token，没有就是过期
            log.info("redis校验fresh_token，id:{}，refreshToken:{}", userId, refreshToken);
            token = redisService.getFromMap(
                    RedisKeyBuilder.UserTokenKey(userId), REFRESH_TOKEN);
            // token为空过期，不等于refreshToken，校验失败
            if (refreshToken == null || !refreshToken.equals(token)) {
                return false;
            }
        } catch (Exception e) {
            log.error("redis校验fresh_token失败！id:{}，token:{}", userId, token);
            // redis宕机，解码校验令牌
            try {
                Claims claims = JwtUtil.parseJWT(secretKey, token);
                // 校验用户id
                if (!claims.get(USER_ID_KEY).equals(userId)) {
                    throw new AppException("用户id与token不匹配！");
                }
                log.info("用户进行jwt校验通过，id:{}，token:{}", userId, token);
                return true;
            } catch (Exception ex) {
                // 不通过
                log.error("用户进行jwt校验失败！id:{}，token:{}", userId, token);;
                return false;
            }
        }
        return true;
    }

    @Override
    public void updateSchoolStudentCount() {
        schoolMapper.listSchoolCode().forEach(code -> {
            Integer studentCount = userMapper.CountStudentBySchoolCode(code);
            schoolMapper.updateSchoolStudentCount(code, studentCount);
        });
    }

    @Override
    public List<UserEntity> queryUserBaseInfo(List<String> userIds) {
        return Optional.ofNullable(userMapper.listUserBaseInfoByUserIds(userIds))
                .map(l -> {
                    Map<String, UserEntity> userMap = l.stream()
                            .map(userConvertor::convert) // 先转换为 UserEntity
                            .collect(Collectors.toMap(UserEntity::getOpenid, user -> user));
                    return userIds.stream()
                            .map(userMap::get)
                            .collect(Collectors.toList());
                })
                .orElse(Collections.emptyList());
    }

    @Override
    public UserEntity queryUserStatus(String userId) {
        return Optional.ofNullable(userMapper.getUserStatusByUserId(userId))
                .map(userConvertor::convert)
                .orElse(null);
    }

    @Override
    public boolean isSchoolCodeValid(String schoolCode) {
        return Optional.ofNullable(schoolCode).map(schoolMapper::getSchoolBySchoolCode).isPresent();
    }

    @Override
    public boolean isUserIdValid(String userId) {
        return Optional.ofNullable(userId).map(userMapper::getUserByUserId).isPresent();
    }

    @Override
    public void deleteUser(String openid) {
        userMapper.deleteUserByUserId(openid);
    }

}
