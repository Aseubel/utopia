package com.aseubel.infrastructure.adapter.repo;

import com.aseubel.domain.bazaar.adapter.repo.IBazaarUserRepository;
import com.aseubel.domain.community.adapter.repo.ICommunityUserRepository;
import com.aseubel.domain.user.adapter.repo.IUserRepository;
import com.aseubel.domain.user.model.entity.UserEntity;
import com.aseubel.infrastructure.convertor.UserConvertor;
import com.aseubel.infrastructure.dao.SchoolMapper;
import com.aseubel.infrastructure.dao.community.CommentMapper;
import com.aseubel.infrastructure.dao.community.FavoriteMapper;
import com.aseubel.infrastructure.dao.community.LikeMapper;
import com.aseubel.infrastructure.dao.po.Comment;
import com.aseubel.infrastructure.dao.po.User;
import com.aseubel.infrastructure.dao.user.UserMapper;
import com.aseubel.infrastructure.redis.IRedisService;
import com.aseubel.types.exception.AppException;
import com.aseubel.types.util.JwtUtil;
import com.aseubel.types.util.RedisKeyBuilder;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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

    @Autowired
    private UserMapper userMapper;

    @Resource
    private UserConvertor userConvertor;

    @Resource
    private IRedisService redisService;

    @Resource
    private SchoolMapper schoolMapper;

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private LikeMapper likeMapper;

    @Resource
    private FavoriteMapper favoriteMapper;

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
        Map<String, Object> claims = new HashMap<>();
        claims.put(USER_ID_KEY, userId);
        return JwtUtil.createJWT(secretKey, ttl, claims);
    }

    @Override
    public void saveUserToken(UserEntity user) {
        redisService.setValue(
                RedisKeyBuilder.userAccessTokenKey(user.getOpenid()),
                user.getAccessToken(),
                ACCESS_EXPIRE_TIME);
        redisService.setValue(
                RedisKeyBuilder.userRefreshTokenKey(user.getOpenid()),
                user.getRefreshToken(),
                REFRESH_EXPIRE_TIME);
    }

    @Override
    public void cleanUserToken(String openid) {
        redisService.remove(RedisKeyBuilder.userAccessTokenKey(openid));
        redisService.remove(RedisKeyBuilder.userRefreshTokenKey(openid));
    }

    @Override
    public boolean checkRefreshToken(UserEntity user, String secretKey) {
        String refreshToken = user.getRefreshToken();
        String userId = user.getOpenid();
        String token = null;
        try {
            // 校验redis中是否有token，没有就是过期
            log.info("redis校验fresh_token，id:{}，refreshToken:{}", userId, refreshToken);
            token = redisService.getValue(RedisKeyBuilder.userRefreshTokenKey(userId));
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
                log.error("用户进行jwt校验失败！id:{}，token:{}", userId, token);
                ;
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
    public Map<String, UserEntity> queryUserBaseInfo(List<String> userIds) {
        return Optional.ofNullable(userMapper.listUserBaseInfoByUserIds(userIds))
                .map(l -> l.stream()
                        .map(userConvertor::convert) // 先转换为 UserEntity
                        .collect(Collectors.toMap(UserEntity::getOpenid, user -> user)))
                .orElse(Collections.emptyMap());
    }

    @Override
    public UserEntity queryUserStatus(String userId) {
        return Optional.ofNullable(userMapper.getUserStatusByUserId(userId))
                .map(userConvertor::convert)
                .orElse(null);
    }

    @Override
    public Map<String, String> queryUserNames(List<String> userIds) {
        List<User> users = userMapper.listUserBaseInfoByUserIds(userIds);
        return users.stream()
                .collect(Collectors.toMap(User::getUserId, User::getUserName));

    }

    @Override
    public Map<String, String> queryUserNamesByCommentIds(List<String> commentIds) {
        List<Comment> comments = commentMapper.listCommentIdAndUserIdByCommentIds(commentIds);
        // 构建commentId-userId映射
        Map<String, String> commentIdUserIdMap = comments.stream()
                .collect(Collectors.toMap(Comment::getCommentId, Comment::getUserId));
        // 批量查询用户名
        List<User> users = userMapper.listUserBaseInfoByUserIds((comments.stream().map(Comment::getUserId).collect(Collectors.toList())));
        // 构建userId-userName映射
        Map<String, String> userIdUserNameMap = users.stream()
                .collect(Collectors.toMap(User::getUserId, User::getUserName));
        // 构建commentId-userName映射
        return commentIdUserIdMap.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> userIdUserNameMap.get(e.getValue())));
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

    @Override
    public void deleteUserToPost(String userId, String postId) {
        List<String> toIds = new ArrayList<>();
        toIds.add(postId);
        toIds.addAll(commentMapper.listCommentIdsByUserIdAndPostId(userId, postId));
        likeMapper.deleteLikeByToIds(toIds);
        commentMapper.deleteCommentByPostId(postId);
    }

}
