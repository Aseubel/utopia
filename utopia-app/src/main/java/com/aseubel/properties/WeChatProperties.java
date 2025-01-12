package com.aseubel.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "wechat.config")
@Data
public class WeChatProperties {

    /** 小程序的appid */
    private String appid = "wx1234567890abcdef";

    /** 小程序的秘钥 */
    private String secret = "1234567890abcdef1234567890abcdef";

}
