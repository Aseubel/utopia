package com.aseubel.config;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;

/**
 * @author Aseubel
 * @date 2025-03-14 22:52
 */
@Configuration
@EnableConfigurationProperties(CanalClientConfigProperties.class)
public class CanalClientConfig {

    @Bean(name = "canalConnector")
    public CanalConnector canalConnector(ConfigurableApplicationContext applicationContext, CanalClientConfigProperties properties) {
        return CanalConnectors.newSingleConnector(
                new InetSocketAddress(properties.getHost(), properties.getPort()),
                properties.getDestination(),
                properties.getUsername(),
                properties.getPassword());
    }
}
