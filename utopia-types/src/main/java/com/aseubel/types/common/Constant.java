package com.aseubel.types.common;

public class Constant {

    public static final String APP = "utopia";
    // 过期时间，单位毫秒
    public static final Long EXPIRE_TIME = 30 * 24 * 3600 * 1000L; // 一个月

    public static final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";

    public static final String USER_ID_KEY = "openid";

    public static final String ACCESS_TOKEN = "access_token";

    public static final String REFRESH_TOKEN = "refresh_token";

    public static final String ENDPOINT = "oss-cn-guangzhou.aliyuncs.com";

    public static final String BUCKET_NAME = "yangaseubel";  // 填写Bucket名称，例如examplebucket。

    public static final String REGION = "cn-guangzhou"; // 填写Bucket所在地域。以华东1（杭州）为例，Region填写为cn-hangzhou。

    public static final long FILE_PART_SIZE = 1 * 1024 * 1024L;   // 1 MB

}
