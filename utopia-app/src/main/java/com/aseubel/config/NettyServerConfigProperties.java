package com.aseubel.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "netty.server", ignoreInvalidFields = true)
public class NettyServerConfigProperties {

    private int port = 8080;

}
