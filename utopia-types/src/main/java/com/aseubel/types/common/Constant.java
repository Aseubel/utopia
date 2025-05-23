package com.aseubel.types.common;

import io.netty.util.AttributeKey;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Constant {

    public static final String APP = "utopia";

    public static final String AVATAR = "avatar";

    public static final String TRADE_POST_IMAGE = "trade_post_image";

    public static final String DISCUSS_POST_IMAGE = "discuss_post_image";

    public static final String COMMENT_IMAGE = "comment_image";
    // 过期时间，单位毫秒
    public static final long REFRESH_EXPIRE_TIME = 30 * 24 * 3600 * 1000L; // 一个月

    public static final long ACCESS_EXPIRE_TIME = 2 * 3600 * 1000L; // 2小时

    public static final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";

    public static final String USER_ID_KEY = "openid";

    public static final String ENDPOINT = "oss-cn-guangzhou.aliyuncs.com";

    public static final String INTERNAL_ENDPOINT = "oss-cn-guangzhou-internal.aliyuncs.com";

    public static final String BUCKET_NAME = "yangaseubel";  // 填写Bucket名称，例如examplebucket。

    public static final String REGION = "cn-guangzhou"; // 填写Bucket所在地域。以华东1（杭州）为例，Region填写为cn-hangzhou。

    public static final long FILE_PART_SIZE = 1 * 1024 * 1024L;   // 1 MB

    public static final Integer PER_PAGE_DISCUSS_POST_SIZE = 20;

    public static final Integer PER_PAGE_COMMENT_SIZE = 10;

    public static final Integer PER_PAGE_SUB_COMMENT_SIZE = 10;

    public static final Integer PER_PAGE_TRADE_POST_SIZE = 20;

    public static final Integer PER_PAGE_FILE_SIZE = 20;

    public static final int NETTY_PORT = 21611;

    public static final AttributeKey<String> WS_TOKEN_KEY = AttributeKey.valueOf("code");
    public static final AttributeKey<String> WS_USER_ID_KEY = AttributeKey.valueOf("userId");

    public static final long RECENT_POST_CACHE_EXPIRE_TIME = 6 * 60 * 1000L; // 6分钟

    public static final long COURSES_EXPIRE_TIME = 24 * 3600 * 1000L; // 一天

    public static final long REPEAT_DOWNLOAD_EXPIRE_TIME = 2 * 3600 * 1000L; // 两小时

    public static final long COMMENT_CACHE_EXPIRE_TIME = 7 * 24 * 3600 * 1000L; // 七天

    public static final long POST_CACHE_EXPIRE_TIME = 7 * 24 * 3600 * 1000L; // 七天

    public static String getDiscussPostSearchIndex(String schoolCode) {
        return "dp_" + schoolCode;
    }

    public static String getTradePostSearchIndex(String schoolCode) {
        return "tp_" + schoolCode;
    }

    public static final String FILE_SEARCH_INDEX = "file_index";

    public static final String[] SEARCH_FIELD = new String[] {"title", "content"};

    public static final String[] FILE_SEARCH_FIELD = new String[] {"fileName", "courseName"};

    public static final String[] SEARCH_SORT = new String[] {"likeCount:desc", "commentCount:desc", "favoriteCount:desc"};

    public static final String[] SEARCH_SORT_SETTINGS = new String[] {"likeCount", "commentCount", "favoriteCount"};

    public static final String[] FILE_SEARCH_SORT_SETTINGS = new String[] {"downloadCount"};

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static final int NOTICE_TYPE_COMMENT_POST = 0;

    public static final int NOTICE_TYPE_COMMENT_REPLY = 1;

    public static final String DIFY_WORK_FLOW_RESPONSE_MODE = "streaming";

    public static final LocalDateTime START_CREATION_TIME = LocalDateTime.of(2025, 4, 13, 0, 0, 0);

    public static final Integer COMMEND_POST_NEIGHBOR_SIZE = 2;

    public static final Integer COMMEND_POST_SIZE = 3;

    public static final Integer COMMENT_CES_SCORE = 2;

    public static final Integer LIKE_CES_SCORE = 2;

    public static final Integer FAVORITE_CES_SCORE = 4;

    public static final String LIKE_POST = "like";

    public static final String FAVORITE_POST = "favorite";

    public static final String COMMENT_POST = "comment";
}
