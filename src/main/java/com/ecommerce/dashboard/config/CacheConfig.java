package com.ecommerce.dashboard.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfig {

    @Value("${cache.stats-ttl:300}")
    private int statsTtl;

    @Value("${cache.chart-ttl:600}")
    private int chartTtl;

    @Value("${cache.products-ttl:300}")
    private int productsTtl;

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager manager = new CaffeineCacheManager("dashboardStats", "salesChart", "topProducts");
        // set a default Caffeine builder (will be used for caches)
        manager.setCaffeine(Caffeine.newBuilder().expireAfterWrite(Duration.ofSeconds(statsTtl)).maximumSize(500));
        return manager;
    }
}
