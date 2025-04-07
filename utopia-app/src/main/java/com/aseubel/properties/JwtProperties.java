package com.aseubel.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt.config")
@Data
public class JwtProperties {

    private String secretKey = "aseubel-secret-key";

    private Long refresh_ttl = 604800017L;    // refresh token 过期时间，默认一星期

    private Long access_ttl = 7200000L;    // access token 过期时间，默认两小时

    private String tokenName = "AccessToken";
}
