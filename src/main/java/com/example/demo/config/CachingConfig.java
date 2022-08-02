package com.example.demo.config;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotNull;
import java.util.concurrent.TimeUnit;

@Configuration
public class CachingConfig {

    /**
     * stateCache.
     */
    @Bean
    public Cache<String, String> stateCache(DemoConfig configProperties) {
        return CacheBuilder.newBuilder()
                .expireAfterAccess(configProperties.getExpireCacheTime(), TimeUnit.MILLISECONDS)
                .build(loader());
    }

    @NotNull
    private CacheLoader<String, String> loader() {
        return new CacheLoader<>() {
            @Override
            public @NotNull String load(@NotNull String key) {
                return key;
            }
        };
    }
}
