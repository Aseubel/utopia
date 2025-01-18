package com.aseubel.types.util;

import static com.aseubel.types.common.RedisKey.*;

/**
 * @author Aseubel
 * @description redisKey构造
 * @date 2025-01-15 09:09
 */
public class RedisKeyBuilder {

    /**
     * 构造用户token的redis key
     * utopia:user:token:{userId}
     * @param userId 用户id
     * @return redis key
     */
    public static String UserTokenKey(String userId) {
        return PREFIX + SPLIT + USER_TOKEN + SPLIT + userId;
    }

    /**
     * 构造用户信息的redis key
     * utopia:user:info:{userId}
     * @param userId 用户id
     * @return redis key
     */
    public static String UserInfoKey(String userId) {
        return PREFIX + SPLIT + USER_INFO + SPLIT + userId;
    }

}
