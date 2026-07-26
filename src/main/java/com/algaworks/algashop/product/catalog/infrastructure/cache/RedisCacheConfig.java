package com.algaworks.algashop.product.catalog.infrastructure.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.cache.autoconfigure.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;

import java.time.Duration;

@Configuration
@EnableCaching
@ConditionalOnProperty(name = "spring.cache.type", havingValue = "redis")
@RequiredArgsConstructor
public class RedisCacheConfig implements CachingConfigurer {

    private final ResilienceCacheErrorHandler resilienceCacheErrorHandler;

    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .computePrefixWith(c -> c + ":")
                .entryTtl(Duration.ofMinutes(1));
        return builder -> builder.cacheDefaults(defaultCacheConfig)
                .withCacheConfiguration("algashop:products:v1",
                        defaultCacheConfig.disableCachingNullValues().entryTtl(Duration.ofMinutes(5)));
    }

    @Bean
    @Override
    public CacheErrorHandler errorHandler() {
        return resilienceCacheErrorHandler;
    }
}
