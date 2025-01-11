package com.aseubel.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt.config")
@Data
public class JwtProperties {

    private String secretKey = "aseubel-secret-key";

    private Long ttl = 604800017L;    // 过期时间，默认一星期

    private String tokenName = "Authorization";
}
