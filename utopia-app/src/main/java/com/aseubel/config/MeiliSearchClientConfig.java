package com.aseubel.config;

import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Config;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(MeiliSearchClientProperties.class)
public class MeiliSearchClientConfig {

    @Bean("meilisearchClient")
    public Client meilisearchClient(ConfigurableApplicationContext applicationContext, MeiliSearchClientProperties meiliSearchClientProperties) {
        Config config = new Config(meiliSearchClientProperties.getHostUrl(), meiliSearchClientProperties.getApiKey());
        return new Client(config);
    }

}
