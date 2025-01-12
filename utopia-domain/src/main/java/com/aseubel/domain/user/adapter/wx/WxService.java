package com.aseubel.domain.user.adapter.wx;

/**
 * @author Aseubel
 * @description 微信服务接口
 * @date 2025-01-12 19:09
 */
public interface WxService {

    /**
     * 获取微信用户的OpenId
     * @param appid 小程序的appid
     * @param secret 小程序的secret
     * @param code 登录凭证
     * @return OpenId
     */
    String getOpenid(String appid, String secret, String code);

}
