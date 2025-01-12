package com.aseubel.infrastructure.adapter.repo;

import com.aseubel.domain.user.adapter.repo.IUserRepository;
import com.aseubel.domain.user.model.UserEntity;
import com.aseubel.infrastructure.convertor.UserConvertor;
import com.aseubel.infrastructure.dao.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

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

}
