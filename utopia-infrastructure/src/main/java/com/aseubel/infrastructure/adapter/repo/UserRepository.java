package com.aseubel.infrastructure.adapter.repo;

import com.aseubel.domain.user.adapter.repo.IUserRepository;
import com.aseubel.domain.user.model.UserEntity;
import com.aseubel.infrastructure.convertor.UserConvertor;
import com.aseubel.infrastructure.dao.UserMapper;
import com.aseubel.types.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.aseubel.types.common.Constant.USER_ID_KEY;

/**
 * @author Aseubel
 * @description 用户数据访问层实现类
 * @date 2025-01-12 17:37
 */
@Repository
@Slf4j
public class UserRepository implements IUserRepository {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserConvertor userConvertor;

    @Override
    public UserEntity queryUserInfo(String userId) {
        return Optional.ofNullable(userMapper.getUserByUserId(userId))
                .map(userConvertor::convert)
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

}
