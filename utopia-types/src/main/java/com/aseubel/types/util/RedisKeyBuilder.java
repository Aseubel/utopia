package com.aseubel.types.util;

import static com.aseubel.types.common.RedisKey.*;

/**
 * @author Aseubel
 * @description redisKey构造
 * @date 2025-01-15 09:09
 */
public class RedisKeyBuilder {

    public static String UserTokenKey(String userId) {
        return PREFIX + SPLIT + USER_TOKEN + SPLIT + userId;
    }

    public static String UserInfoKey(String userId) {
        return PREFIX + SPLIT + USER_INFO + SPLIT + userId;
    }

}
