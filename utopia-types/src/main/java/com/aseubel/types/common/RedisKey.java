package com.aseubel.types.common;

/**
 * @author Aseubel
 * @description Redis key
 * @date 2025-01-15 08:25
 */
public class RedisKey {

    public static final String SPLIT = ":";

    public static final String PREFIX = "utopia";

    public static final String USER = "user";

    public static final String USER_TOKEN = "user:token";

    public static final String ACCESS_TOKEN = "access_token";

    public static final String REFRESH_TOKEN = "refresh_token";

    public static final String USER_INFO = "user:info";

    public static final String COMMUNITY = "community";

    public static final String DISCUSS_POST = "community:post";

    public static final String DISCUSS_COMMENT = "community:comment";

    public static final String LIKE_COUNT = "lc";

    public static final String LIKE_STATUS = "ls";

    public static final String FAVORITE_COUNT = "fc";

    public static final String FAVORITE_STATUS = "fs";

    public static final String REDIS_OFFLINE_KEY = "offline:msgs:";

}
