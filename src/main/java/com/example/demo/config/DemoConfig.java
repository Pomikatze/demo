package com.example.demo.config;

import com.example.demo.config.feign.YamlPropertyLoaderFactory;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.validation.annotation.Validated;

/**
 * Переменные окружения.
 */
@ConfigurationProperties(prefix = "demo.test")
@Configuration
@PropertySource(value = "classpath:/demo-application.yml",
        factory = YamlPropertyLoaderFactory.class)
@Getter
@Setter
@Validated
public class DemoConfig {

    private Long expireCacheTime = 120000L;
}
