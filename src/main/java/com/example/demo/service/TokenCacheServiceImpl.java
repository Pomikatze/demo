package com.example.demo.service;

import com.example.demo.service.api.TokenCacheService;
import com.google.common.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Сервис для кеширования выданных токенов.
 */
@Service
@Slf4j
public class TokenCacheServiceImpl implements TokenCacheService {

    private final Cache<String, String> stateTokenCache;

    public TokenCacheServiceImpl(@Qualifier("stateCache") Cache<String, String> stateTokenCache) {
        this.stateTokenCache = stateTokenCache;
    }

    @Override
    public Optional<String> getIfPresent(String state) {
        return Optional.ofNullable(stateTokenCache.getIfPresent(state));
    }

    @Override
    public void put(String state, String token) {
        stateTokenCache.put(state, token);
    }

    @Override
    public void invalidate(String state) {
        stateTokenCache.invalidate(state);
    }
}
