package com.cyberpunk.elasticsearch.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author lujun
 * @date 2023/8/21 14:37
 */
@Component
@Getter
@Setter
@ConfigurationProperties(ElasticSearchClientProperties.PREFIX)
public class ElasticSearchClientProperties {

    public static final String PREFIX = "spring.es";

    private String serverUrl;

    private String apiKey;

    private boolean enabled;


}
