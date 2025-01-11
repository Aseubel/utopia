package com.aseubel.wechat;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "wechat.config")
@Data
public class WeChatProperties {

    /** 小程序的appid */
    private String appid;

    /** 小程序的秘钥 */
    private String secret;

}
