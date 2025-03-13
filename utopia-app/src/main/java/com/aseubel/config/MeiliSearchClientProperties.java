package com.aseubel.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "meilisearch.sdk.config", ignoreInvalidFields = true)
public class MeiliSearchClientProperties {

    /** hostUrl */
    private String hostUrl = "http://localhost:7700";
    /** ApiKey */
    private String apiKey = "masterKey";

}
