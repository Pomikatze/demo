package com.example.demo.config.feign;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * The type Feign services properties.
 */
@Data
@ConfigurationProperties(prefix = "feign.common")
@Configuration
public class CommonFeignProperties {
    private int connectTimeoutMillis;
    private int readTimeoutMillis;
    private String url;
}
