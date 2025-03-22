package com.aseubel.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Aseubel
 * @date 2025-03-14 18:54
 */
@Data
//@ConfigurationProperties(prefix = "canal.client")
public class CanalClientConfigProperties {

    /** 目标canal实例名称 */
    private String destination = "example";
    /** 用户名 */
    private String username = "canal";
    /** 密码 */
    private String password = "canal";
    /** 连接地址 */
    private String host = "127.0.0.1";
    /** 端口 */
    private int port = 11111;
    /** 单次获取数量 */
    private int batchSize = 100;
    /** 轮询间隔 */
    private long interval = 1000;

}
